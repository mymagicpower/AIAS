package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.Lac;
import me.aias.example.utils.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 中文翻译为英文
 * Translate Chinese to English
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TranslationExample {

    private static final Logger logger = LoggerFactory.getLogger(TranslationExample.class);

    private TranslationExample() {
    }

    public static void main(String[] args) throws IOException, TranslateException, ModelException {

        // 分词 - Split words
        Lac lac = new Lac();
        Criteria<String, String[][]> lacCriteria = lac.criteria();
        // 翻译 - Translation
        Translation senta = new Translation();
        Criteria<String[], String[]> SentaCriteria = senta.criteria();

        try (ZooModel<String, String[][]> lacModel = lacCriteria.loadModel();
             Predictor<String, String[][]> lacPredictor = lacModel.newPredictor();
             ZooModel<String[], String[]> sentaModel = SentaCriteria.loadModel();
             Predictor<String[], String[]> sentaPredictor = sentaModel.newPredictor()) {

            String input = "今天天气怎么样？";
            logger.info("输入句子: {}", input);

            String[][] lacResult = lacPredictor.predict(input);
            // 分词 - Split words
            logger.info("Words : " + Arrays.toString(lacResult[0]));
            // 词性 - tag
            logger.info("Tags : " + Arrays.toString(lacResult[1]));

            // 翻译结果 - Translation result
            String[] translationResult = sentaPredictor.predict(lacResult[0]);
            for (int i = 0; i < translationResult.length; i++) {
                logger.info("T" + i + ": " + translationResult[i]);
            }
//            logger.info(Arrays.toString(translationResult));
        }
    }
}
