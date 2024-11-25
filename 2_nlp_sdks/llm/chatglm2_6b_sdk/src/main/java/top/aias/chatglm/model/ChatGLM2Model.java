package top.aias.chatglm.model;

import ai.djl.Device;
import ai.djl.ModelException;
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
import top.aias.chatglm.tokenizer.SpProcessor;
import top.aias.chatglm.utils.CausalLMOutput;
import top.aias.chatglm.utils.GreedyBatchTensorList;
import top.aias.chatglm.utils.SearchConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class ChatGLM2Model implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ChatGLM2Model.class);
    private SearchConfig config;
    private ZooModel<NDList, NDList> chatGLM2Model;
    private Predictor<int[], CausalLMOutput> encoderPredictor;
    private Predictor<NDList, CausalLMOutput> decoderPredictor;
    private NDManager manager;

    public ChatGLM2Model(SearchConfig config, String modelPath, Device device) throws ModelException, IOException {
        this.config = config;
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(modelPath))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optTranslator(new NoopTranslator())
                        .build();

        manager = NDManager.newBaseManager(device);
        chatGLM2Model = criteria.loadModel();
        encoderPredictor = chatGLM2Model.newPredictor(new EncoderTranslator());
        decoderPredictor = chatGLM2Model.newPredictor(new DecoderTranslator());
    }

    public NDArray sample(SpProcessor processor, int[] ids) throws TranslateException {
        CausalLMOutput encoderOutput = encoder(ids);
        encoderOutput.getLogits().attach(manager);
        encoderOutput.getPastKeyValuesList().attach(manager);

        NDArray inputIdsArray = greedyStepGen(config, encoderOutput.getLogits());

        int[] intArray = inputIdsArray.toType(DataType.INT32, true).toIntArray();
        String result = processor.decode(intArray);
        logger.info("{}", result);

        long[] attentionMask = new long[ids.length + 2];
        Arrays.fill(attentionMask, 1);
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);

        GreedyBatchTensorList searchState =
                new GreedyBatchTensorList(inputIdsArray, inputIdsArray, encoderOutput.getPastKeyValuesList(), attentionMaskArray);

        while (true) {
            try (NDScope ignore = new NDScope()) {
                NDArray pastOutputIds = searchState.getPastOutputIds();
                NDArray nextInputIds = searchState.getNextInputIds();
                NDArray pastAttentionMask = searchState.getPastAttentionMask();
                NDList pastKeyValues = searchState.getPastKeyValues();
                long pastSeqLength = pastAttentionMask.getShape().getLastDimension();
                NDList modelInput = prepareInput(nextInputIds, pastAttentionMask, pastSeqLength);
                if (pastKeyValues != null) {
                    modelInput.addAll(pastKeyValues);
                }
                CausalLMOutput modelOutput = decoder(modelInput);

                NDArray outputIds = greedyStepGen(config, modelOutput.getLogits());

                intArray = outputIds.toType(DataType.INT32, true).toIntArray();
                result = processor.decode(intArray);
                logger.info("{}", result);

                nextInputIds = outputIds;
                searchState.setNextInputIds(nextInputIds);

                pastOutputIds = pastOutputIds.concat(nextInputIds, 1);
                searchState.setPastOutputIds(pastOutputIds);

                pastKeyValues = modelOutput.getPastKeyValuesList();
                searchState.setPastKeyValues(pastKeyValues);
                pastAttentionMask = pastAttentionMask.concat(manager.ones(new Shape(1, 1), DataType.INT64), 1);
                searchState.setPastAttentionMask(pastAttentionMask);

                // memory management
                NDScope.unregister(nextInputIds, pastAttentionMask, pastOutputIds);
                NDScope.unregister(pastKeyValues);
            }

            // Termination Criteria
            // TODO: <EOS>, delete the sentence and add it to result.
            long id = searchState.getNextInputIds().toLongArray()[0];
            if (config.getEosTokenId() == id) {
                searchState.setNextInputIds(null);
                break;
            }
            if (searchState.getPastOutputIds() != null && searchState.getPastOutputIds().getShape().get(1) + 1 >= config.getMaxSeqLength()) {
                break;
            }
        }

        if(searchState.getNextInputIds() == null){
            return searchState.getPastOutputIds();
        }else {
            return searchState.getPastOutputIds().concat(searchState.getNextInputIds(), 1);
        }
    }

    private NDList prepareInput(
            NDArray inputIds, NDArray attentionMask, long pastSeqLength) {
        // Pack the model input
        NDArray positionIds =
                inputIds.getManager()
                        .arange(
                                pastSeqLength,
                                pastSeqLength + inputIds.getShape().getLastDimension(),
                                1,
                                DataType.INT64)
                        .expandDims(0)
                        .repeat(0, inputIds.getShape().get(0));

        attentionMask = attentionMask.concat(manager.ones(new Shape(1, 1), DataType.INT64), 1);

        return new NDList(inputIds, positionIds, attentionMask);
    }

    public CausalLMOutput encoder(int[] ids) throws TranslateException {
        return encoderPredictor.predict(ids);
    }

    public CausalLMOutput decoder(NDList input) throws TranslateException {
        return decoderPredictor.predict(input);
    }

    @Override
    public void close() {
        encoderPredictor.close();
        decoderPredictor.close();
        chatGLM2Model.close();
        manager.close();
    }

    public NDArray greedyStepGen(SearchConfig config, NDArray next_token_scores) {
        // logits:  [batch, seq, probDim]
        assert next_token_scores.getShape().getShape().length == 3 : "unexpected input";
        next_token_scores = next_token_scores.get(":, -1, :");
        boolean isNaN = next_token_scores.isNaN().any().toBooleanArray()[0];
        boolean isInfinite = next_token_scores.isInfinite().any().toBooleanArray()[0];
        if (isNaN || isInfinite) {
            next_token_scores = next_token_scores.zerosLike();
            next_token_scores.set(new NDIndex("...,5"), 5e4);
        }

        // logits_warper 1. TemperatureLogitsWarper
        next_token_scores = next_token_scores.div(config.getTemperature());

        long topK = Math.min(config.getTopK(), next_token_scores.getShape().getLastDimension());

        // logits_warper 2. TopKLogitsWarper
        // last token of the top-k
        NDList ndList = next_token_scores.topK((int) topK, -1);
        NDArray values = ndList.get(0);
        NDArray indices = ndList.get(1);
        float lastOfTopK = values.get(":, -1").toFloatArray()[0];
        NDArray indices_to_remove = next_token_scores.lt(lastOfTopK);
        next_token_scores.set(indices_to_remove, Float.MIN_VALUE);

        // logits_warper 3. TopPLogitsWarper
//        next_token_scores = NDManager.newBaseManager().create(new int[]{3,4,1,2}).reshape(1,4);
        NDArray sorted_indices = next_token_scores.argSort(-1, true);
        NDArray sorted_logits = next_token_scores.sort(1);
//        NDArray sorted_logits = next_token_scores.get(sorted_indices);
        NDArray cumulative_probs = sorted_logits.softmax(-1).cumSum(-1);
        // Remove tokens with cumulative top_p above the threshold (token with 0 are kept)
        NDArray sorted_indices_to_remove = cumulative_probs.lte(1 - config.getTopP());
        // scatter sorted tensors to original indexing
        indices_to_remove = sorted_indices_to_remove.scatter(sorted_indices, sorted_indices_to_remove, 1);
        next_token_scores.set(indices_to_remove, Float.MIN_VALUE);

        // sample
        NDArray probs = next_token_scores.softmax(-1);
        NDArray next_tokens = probs.argMax(-1);


        return next_tokens.expandDims(0); // [batch, vacDim]
    }

}
