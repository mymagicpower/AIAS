package top.aias.gpt2;

import ai.djl.MalformedModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.generate.CausalLMOutput;
import ai.djl.modality.nlp.generate.SearchConfig;
import ai.djl.modality.nlp.generate.TextGenerator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Function;

public final class TextGeneration {

    private static final Logger logger = LoggerFactory.getLogger(TextGeneration.class);

    private TextGeneration() {}

    public static void main(String[] args)
            throws ModelNotFoundException, MalformedModelException, IOException,
                    TranslateException {
        String ret = generateTextWithPyTorch();
        logger.info("{}", ret);
    }

    public static String generateTextWithPyTorch()
            throws ModelNotFoundException, MalformedModelException, IOException,
                    TranslateException {
        SearchConfig config = new SearchConfig();
        config.setMaxSeqLength(60);

        long kvDim = 64;
        int numAttentionHeads = 12;
        int numLayers = 12;

        Criteria<NDList, CausalLMOutput> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, CausalLMOutput.class)
                        .optModelPath(Paths.get("models/gpt2.pt"))
                        .optEngine("PyTorch")
                        .optTranslator(new PtGptTranslator(kvDim, numAttentionHeads, numLayers))
                        .build();
        String input = "DeepMind Company is";

        try (ZooModel<NDList, CausalLMOutput> model = criteria.loadModel();
                Predictor<NDList, CausalLMOutput> predictor = model.newPredictor();
                NDManager manager = model.getNDManager().newSubManager();
                HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance("gpt2")) {

            TextGenerator generator = new TextGenerator(predictor, "greedy", config);

            Encoding encoding = tokenizer.encode(input);
            long[] inputIds = encoding.getIds();
            NDArray inputIdArray = manager.create(inputIds).expandDims(0);

            NDArray output = generator.greedySearch(inputIdArray);
            long[] outputIds = output.toLongArray();
            return tokenizer.decode(outputIds);
        }
    }
}