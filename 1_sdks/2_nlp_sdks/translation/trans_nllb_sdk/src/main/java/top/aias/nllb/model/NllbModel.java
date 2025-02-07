package top.aias.nllb.model;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.NDScope;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.nllb.generate.CausalLMOutput;
import top.aias.nllb.generate.GreedyBatchTensorList;
import top.aias.nllb.generate.SearchConfig;
import top.aias.nllb.tokenizer.TokenUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
/**
 * 模型载入及推理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class NllbModel implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(NllbModel.class);
    private SearchConfig config;
    private ZooModel<NDList, NDList> nllbModel;
    private HuggingFaceTokenizer tokenizer;
    private Predictor<long[], NDArray> encoderPredictor;
    private Predictor<NDList, CausalLMOutput> decoderPredictor;
    private Predictor<NDList, CausalLMOutput> decoder2Predictor;
    private NDManager manager;

    public NllbModel(SearchConfig config, String modelPath, String modelName, Device device) throws ModelException, IOException {
        this.config = config;
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optTranslator(new NoopTranslator())
                        .build();

        manager = NDManager.newBaseManager(device);
        nllbModel = criteria.loadModel();
        tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(modelPath + "tokenizer.json"));
        encoderPredictor = nllbModel.newPredictor(new EncoderTranslator());
        decoderPredictor = nllbModel.newPredictor(new DecoderTranslator());
        decoder2Predictor = nllbModel.newPredictor(new Decoder2Translator());
    }

    public NDArray encoder(long[] ids) throws TranslateException {
        return encoderPredictor.predict(ids);
    }

    public CausalLMOutput decoder(NDList input) throws TranslateException {
        return decoderPredictor.predict(input);
    }

    public CausalLMOutput decoder2(NDList input) throws TranslateException {
        return decoder2Predictor.predict(input);
    }

    @Override
    public void close() {
        encoderPredictor.close();
        decoderPredictor.close();
        decoder2Predictor.close();
        nllbModel.close();
        manager.close();
        tokenizer.close();
    }

    public String translate(String input) throws TranslateException {

        Encoding encoding = tokenizer.encode(input);
        long[] ids = encoding.getIds();
        // 1. Encoder
        long[] inputIds = new long[ids.length];
        // 设置源语言编码
        inputIds[0] = config.getSrcLangId();
        for (int i = 0; i < ids.length - 1; i++) {
            inputIds[i + 1] = ids[i];
        }
        logger.info("inputIds: " + Arrays.toString(inputIds));
        long[] attentionMask = encoding.getAttentionMask();
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);

        NDArray encoderHiddenStates = encoder(inputIds);

        NDArray decoder_input_ids = manager.create(new long[]{config.getDecoderStartTokenId()}).reshape(1, 1);
        NDList decoderInput = new NDList(decoder_input_ids, encoderHiddenStates, attentionMaskArray);

        // 2. Initial Decoder
        CausalLMOutput modelOutput = decoder(decoderInput);
        modelOutput.getLogits().attach(manager);
        modelOutput.getPastKeyValuesList().attach(manager);

        GreedyBatchTensorList searchState =
                new GreedyBatchTensorList(null, decoder_input_ids, modelOutput.getPastKeyValuesList(), encoderHiddenStates, attentionMaskArray);

        while (true) {
//            try (NDScope ignore = new NDScope()) {
            NDArray pastOutputIds = searchState.getPastOutputIds();

            if (searchState.getNextInputIds() != null) {
                decoderInput = new NDList(searchState.getNextInputIds(), searchState.getEncoderHiddenStates(), searchState.getAttentionMask());
                decoderInput.addAll(searchState.getPastKeyValues());
                // 3. Decoder loop
                modelOutput = decoder2(decoderInput);
            }

            NDArray outputIds = greedyStepGen(config, pastOutputIds, modelOutput.getLogits());

            searchState.setNextInputIds(outputIds);
            pastOutputIds = pastOutputIds.concat(outputIds, 1);
            searchState.setPastOutputIds(pastOutputIds);

            searchState.setPastKeyValues(modelOutput.getPastKeyValuesList());

            // memory management
//                NDScope.unregister(outputIds, pastOutputIds);
//        }

            // Termination Criteria
            long id = searchState.getNextInputIds().toLongArray()[0];
            if (config.getEosTokenId() == id) {
                searchState.setNextInputIds(null);
                break;
            }
            if (searchState.getPastOutputIds() != null && searchState.getPastOutputIds().getShape().get(1) + 1 >= config.getMaxSeqLength()) {
                break;
            }
        }

        if (searchState.getNextInputIds() == null) {
            NDArray resultIds = searchState.getPastOutputIds();
            String result = TokenUtils.decode(config, tokenizer, resultIds);
            return result;
        } else {
            NDArray resultIds = searchState.getPastOutputIds(); // .concat(searchState.getNextInputIds(), 1)
            String result = TokenUtils.decode(config, tokenizer, resultIds);
            return result;
        }

    }

    public NDArray greedyStepGen(SearchConfig config, NDArray pastOutputIds, NDArray next_token_scores) {
        next_token_scores = next_token_scores.get(":, -1, :");

        NDArray new_next_token_scores = manager.create(next_token_scores.getShape(), next_token_scores.getDataType());
        next_token_scores.copyTo(new_next_token_scores);

        // LogitsProcessor 1. ForcedBOSTokenLogitsProcessor
        // 设置目标语言
        long cur_len = pastOutputIds.getShape().getLastDimension();
        if (cur_len == 1) {
            long num_tokens = new_next_token_scores.getShape().getLastDimension();
            for (long i = 0; i < num_tokens; i++) {
                if (i != config.getForcedBosTokenId()) {
                    new_next_token_scores.set(new NDIndex(":," + i), Float.NEGATIVE_INFINITY);
                }
            }
            new_next_token_scores.set(new NDIndex(":," + config.getForcedBosTokenId()), 0);
        }

        NDArray probs = new_next_token_scores.softmax(-1);
        NDArray next_tokens = probs.argMax(-1);

        return next_tokens.expandDims(0);
    }

}
