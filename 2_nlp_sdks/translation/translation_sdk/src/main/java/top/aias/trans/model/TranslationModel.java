package top.aias.trans.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.trans.generate.BeamBatchTensorList;
import top.aias.trans.generate.CausalLMOutput;
import top.aias.trans.generate.SearchConfig;
import top.aias.trans.tokenizer.SpTokenizer;
import top.aias.trans.utils.NDArrayUtils;
import top.aias.trans.utils.TokenUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 模型载入及推理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TranslationModel implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(TranslationModel.class);
    private SearchConfig config;
    private ZooModel<NDList, NDList> transModel;
    private EncoderPool encoderPool;
    private DecoderPool decoderPool;
    private Decoder2Pool decoder2Pool;
    private SpTokenizer sourceTokenizer;
    private NDManager manager;
    private ConcurrentHashMap<String, Long> map;
    private ConcurrentHashMap<Long, String> reverseMap;

    private float length_penalty = 1.0f;
    private boolean do_early_stopping = false;
    private int num_beam_hyps_to_keep = 1;
    private int num_beam_groups = 1;

    public TranslationModel(SearchConfig config, String modelPath, int poolSize, Device device) throws ModelException, IOException {
        init(config, modelPath, poolSize, device);
    }

    public void init(SearchConfig config, String modelPath, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.config = config;
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(modelPath + "traced_translation.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optTranslator(new NoopTranslator())
                        .build();

        manager = NDManager.newBaseManager(device);
        transModel = criteria.loadModel();
        this.encoderPool = new EncoderPool(transModel, poolSize);
        this.decoderPool = new DecoderPool(transModel, poolSize);
        this.decoder2Pool = new Decoder2Pool(transModel, poolSize);

        sourceTokenizer = new SpTokenizer(Paths.get(modelPath + "source.spm"));

        List<String> words = Utils.readLines(Paths.get(modelPath + "vocab.txt"));
        String jsonStr = "";
        for (String line : words) {
            jsonStr = jsonStr + line;
        }

        map = new Gson().fromJson(jsonStr, new TypeToken<ConcurrentHashMap<String, Long>>() {
        }.getType());
        reverseMap = new ConcurrentHashMap<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> next = (Map.Entry<String, Long>) it.next();
            reverseMap.put(next.getValue(), next.getKey());
        }
    }

    public NDArray encoder(int[] ids) throws TranslateException {
        Predictor<int[], NDArray> predictor = encoderPool.getPredictor();
        NDArray result = predictor.predict(ids);
        encoderPool.releasePredictor(predictor);
        return result;
    }

    public CausalLMOutput decoder(NDList input) throws TranslateException {
        Predictor<NDList, CausalLMOutput> predictor = decoderPool.getPredictor();
        CausalLMOutput result = predictor.predict(input);
        decoderPool.releasePredictor(predictor);
        return result;
    }

    public CausalLMOutput decoder2(NDList input) throws TranslateException {
        Predictor<NDList, CausalLMOutput> predictor = decoder2Pool.getPredictor();
        CausalLMOutput result = predictor.predict(input);
        decoder2Pool.releasePredictor(predictor);
        return result;
    }

    @Override
    public void close() {
        this.encoderPool.close();
        this.decoderPool.close();
        this.decoder2Pool.close();
        this.transModel.close();
        this.manager.close();
        this.sourceTokenizer.close();
    }

    /**
     * 翻译
     * beamSearch
     *
     * @param input
     * @return
     * @throws TranslateException
     */
    public String translate(String input) throws TranslateException {
        long numBeam = config.getBeam();
        BeamSearchScorer beamSearchScorer = new BeamSearchScorer((int) numBeam, length_penalty, do_early_stopping, num_beam_hyps_to_keep, num_beam_groups);

        // 1. Encoder
        List<String> tokens = sourceTokenizer.tokenize(input);
        String[] strs = tokens.toArray(new String[]{});
        logger.info("Tokens: " + Arrays.toString(strs));
        int[] sourceIds = new int[tokens.size() + 1];
        sourceIds[tokens.size()] = 0;
        for (int i = 0; i < tokens.size(); i++) {
            sourceIds[i] = map.get(tokens.get(i)).intValue();
        }

        NDArray encoder_hidden_states = encoder(sourceIds);
        encoder_hidden_states = NDArrayUtils.expand(encoder_hidden_states, config.getBeam());

        NDArray decoder_input_ids = manager.create(new long[]{65000}).reshape(1, 1);
        decoder_input_ids = NDArrayUtils.expand(decoder_input_ids, numBeam);

        long[] attentionMask = new long[sourceIds.length];
        Arrays.fill(attentionMask, 1);
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);
        NDArray new_attention_mask = NDArrayUtils.expand(attentionMaskArray, config.getBeam());
        NDList decoderInput = new NDList(decoder_input_ids, encoder_hidden_states, new_attention_mask);

        // 2. Initial Decoder
        CausalLMOutput modelOutput = decoder(decoderInput);
        modelOutput.getLogits().attach(manager);
        modelOutput.getPastKeyValuesList().attach(manager);

        NDArray beam_scores = manager.zeros(new Shape(1, numBeam), DataType.FLOAT32);
        beam_scores.set(new NDIndex(":, 1:"), -1e9);
        beam_scores = beam_scores.reshape(numBeam, 1);

        NDArray input_ids = decoder_input_ids;
        BeamBatchTensorList searchState = new BeamBatchTensorList(null, new_attention_mask, encoder_hidden_states, modelOutput.getPastKeyValuesList());
        NDArray next_tokens;
        NDArray next_indices;

        while (true) {
//            try (NDScope ignore = new NDScope()) {

            if (searchState.getNextInputIds() != null) {
                decoder_input_ids = searchState.getNextInputIds().get(new NDIndex(":, -1:"));
                decoderInput = new NDList(decoder_input_ids, searchState.getEncoderHiddenStates(), searchState.getAttentionMask());
                decoderInput.addAll(searchState.getPastKeyValues());
                // 3. Decoder loop
                modelOutput = decoder2(decoderInput);
            }


            NDArray next_token_logits = modelOutput.getLogits().get(":, -1, :");

            // hack: adjust tokens for Marian. For Marian we have to make sure that the `pad_token_id`
            // cannot be generated both before and after the `nn.functional.log_softmax` operation.
            NDArray new_next_token_logits = manager.create(next_token_logits.getShape(), next_token_logits.getDataType());
            next_token_logits.copyTo(new_next_token_logits);
            new_next_token_logits.set(new NDIndex(":," + config.getPadTokenId()), Float.NEGATIVE_INFINITY);

            NDArray next_token_scores = new_next_token_logits.logSoftmax(1);

            // next_token_scores = logits_processor(input_ids, next_token_scores)
            // 1. NoBadWordsLogitsProcessor
            next_token_scores.set(new NDIndex(":," + config.getPadTokenId()), Float.NEGATIVE_INFINITY);

            // 2. MinLengthLogitsProcessor 没生效
            // 3. ForcedEOSTokenLogitsProcessor
            long cur_len = input_ids.getShape().getLastDimension();
            if (cur_len == (config.getMaxSeqLength() - 1)) {
                long num_tokens = next_token_scores.getShape().getLastDimension();
                for (long i = 0; i < num_tokens; i++) {
                    if(i !=config.getEosTokenId()){
                        next_token_scores.set(new NDIndex(":," + i), Float.NEGATIVE_INFINITY);
                    }
                }
                next_token_scores.set(new NDIndex(":," + config.getEosTokenId()), 0);
            }

            long vocab_size = next_token_scores.getShape().getLastDimension();
            beam_scores = beam_scores.repeat(1, vocab_size);
            next_token_scores = next_token_scores.add(beam_scores);

            // reshape for beam search
            next_token_scores = next_token_scores.reshape(1, numBeam * vocab_size);

            // [batch, beam]
            NDList topK = next_token_scores.topK(Math.toIntExact(numBeam) * 2, 1, true, true);

            next_token_scores = topK.get(0);
            next_tokens = topK.get(1);

            // next_indices = next_tokens // vocab_size
            next_indices = next_tokens.div(vocab_size).toType(DataType.INT64, true);

            // next_tokens = next_tokens % vocab_size
            next_tokens = next_tokens.mod(vocab_size);

            // stateless
            NDList beam_outputs = beamSearchScorer.process(manager, input_ids, next_token_scores, next_tokens, next_indices, config.getPadTokenId(), config.getEosTokenId());

            beam_scores = beam_outputs.get(0).reshape(numBeam, 1);
            NDArray beam_next_tokens = beam_outputs.get(1);
            NDArray beam_idx = beam_outputs.get(2);

            // input_ids = torch.cat([input_ids[beam_idx, :], beam_next_tokens.unsqueeze(-1)], dim=-1)
            long[] beam_next_tokens_arr = beam_next_tokens.toLongArray();
            long[] beam_idx_arr = beam_idx.toLongArray();
            NDList inputList = new NDList();
            for (int i = 0; i < numBeam; i++) {
                long index = beam_idx_arr[i];
                NDArray ndArray = input_ids.get(index).reshape(1, input_ids.getShape().getLastDimension());
                ndArray = ndArray.concat(manager.create(beam_next_tokens_arr[i]).reshape(1, 1), 1);
                inputList.add(ndArray);
            }
            input_ids = NDArrays.concat(inputList, 0);


            searchState.setNextInputIds(input_ids);
            searchState.setPastKeyValues(modelOutput.getPastKeyValuesList());

            // Memory management
//                NDScope.unregister(
//                        searchState.getNextInputIds(),
//                        searchState.getPastOutputIds(),
//                        searchState.getPastAttentionMask(),
//                        searchState.getLastProbs());
//                NDScope.unregister(searchState.getPastKeyValues());

            boolean maxLengthCriteria = (input_ids.getShape().getLastDimension() >= config.getMaxSeqLength());
            if (beamSearchScorer.isDone() || maxLengthCriteria) {
                break;
            }

//            }

        }

        long[] sequences = beamSearchScorer.finalize(config.getMaxSeqLength(), config.getEosTokenId());
        String result = TokenUtils.decode(reverseMap, sequences);

        return result;
    }

}
