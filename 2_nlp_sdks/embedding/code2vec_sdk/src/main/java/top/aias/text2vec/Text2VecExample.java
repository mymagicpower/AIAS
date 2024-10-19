package top.aias.text2vec;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.text2vec.utils.FeatureComparison;
import top.aias.text2vec.utils.Code2VecModel;

import java.io.IOException;
import java.util.Arrays;

/**
 * 句向量 - 用于代码搜索
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Text2VecExample {

    private static final Logger logger = LoggerFactory.getLogger(Text2VecExample.class);

    private Text2VecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String test = "This is an example sentence";
        String input1 = "calculate cosine similarity between two vectors";
        String input2 = "  public static float dot(float[] feature1, float[] feature2) {\n" +
                "    float ret = 0.0f;\n" +
                "    int length = feature1.length;\n" +
                "    // dot(x, y)\n" +
                "    for (int i = 0; i < length; ++i) {\n" +
                "      ret += feature1[i] * feature2[i];\n" +
                "    }\n" +
                "\n" +
                "    return ret;\n" +
                "  }";
        String input3 = "  public static float cosineSim(float[] feature1, float[] feature2) {\n" +
                "    float ret = 0.0f;\n" +
                "    float mod1 = 0.0f;\n" +
                "    float mod2 = 0.0f;\n" +
                "    int length = feature1.length;\n" +
                "    for (int i = 0; i < length; ++i) {\n" +
                "      ret += feature1[i] * feature2[i];\n" +
                "      mod1 += feature1[i] * feature1[i];\n" +
                "      mod2 += feature2[i] * feature2[i];\n" +
                "    }\n" +
                "    //    dot(x, y) / (np.sqrt(dot(x, x)) * np.sqrt(dot(y, y))))\n" +
                "    return (float) (ret / Math.sqrt(mod1) / Math.sqrt(mod2));\n" +
                "  }";

//    logger.info("input Sentence1: {}", input1);
//    logger.info("input Sentence2: {}", input2);
//    logger.info("input Sentence2: {}", input3);

        try (Code2VecModel text2VecModel = new Code2VecModel("models/", "all-MiniLM-L12-v2.pt", 1)) {
            float[] embeddings = text2VecModel.predict(test);
            logger.info(" embeddings: {}", Arrays.toString(embeddings));

            float[] embeddings1 = text2VecModel.predict(input1);
            logger.info("Vector dimensions: {}", embeddings1.length);
//      logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
            float[] embeddings2 = text2VecModel.predict(input2);
//      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));
            float[] embeddings3 = text2VecModel.predict(input3);
            logger.info("Code Similarity: {}", FeatureComparison.dot(embeddings1, embeddings2));
            logger.info("Code Similarity: {}", FeatureComparison.dot(embeddings1, embeddings3));
        }
    }
}
