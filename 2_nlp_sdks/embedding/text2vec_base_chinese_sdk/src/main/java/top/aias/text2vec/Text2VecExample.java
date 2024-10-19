package top.aias.text2vec;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.text2vec.utils.FeatureComparison;
import top.aias.text2vec.utils.Text2VecModel;

import java.io.IOException;
import java.util.Arrays;

/**
 * 句向量 支持中文，最大长度128
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
        String input1 = "如何更换花呗绑定银行卡";
        String input2 = "花呗更改绑定银行卡";

        logger.info("input Sentence1: {}", input1);
        logger.info("input Sentence2: {}", input2);


        try (Text2VecModel text2VecModel = new Text2VecModel()) {

            float[] embeddings1 = text2VecModel.encode(input1);
            logger.info("Vector dimensions: {}", embeddings1.length);
            logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
            float[] embeddings2 = text2VecModel.encode(input2);
            logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));

            logger.info("Chinese Similarity: {}", FeatureComparison.cosineSim(embeddings1, embeddings2));
        }
    }
}
