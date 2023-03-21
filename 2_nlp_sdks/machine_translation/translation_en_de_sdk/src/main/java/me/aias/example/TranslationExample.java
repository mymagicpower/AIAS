package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 英文翻译为德文
 * English translation to German
 *
 * @author calvin
 * @mail 179209347@qq.com
 */

public final class TranslationExample {

    private static final Logger logger = LoggerFactory.getLogger(TranslationExample.class);

    private TranslationExample() {
    }

    public static void main(String[] args) throws IOException, TranslateException, ModelException {

        Translation translator = new Translation();
        Criteria<String, String[]> SentaCriteria = translator.criteria();

        try (ZooModel<String, String[]> sentaModel = SentaCriteria.loadModel();
             Predictor<String, String[]> sentaPredictor = sentaModel.newPredictor()) {

            String input = "What are you doing now?";
            logger.info("input Sentence: {}", input);

            // 翻译结果
            // Translation result
            String[] translationResult = sentaPredictor.predict(input);
            for (int i = 0; i < translationResult.length; i++) {
                logger.info("T" + i + ": " + translationResult[i]);
            }

//      logger.info(Arrays.toString(translationResult));

        }
    }
}
