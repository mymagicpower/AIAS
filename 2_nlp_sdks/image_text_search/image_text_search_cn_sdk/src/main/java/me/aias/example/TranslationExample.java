package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.Jieba;
import me.aias.translation.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public final class TranslationExample {

  private static final Logger logger = LoggerFactory.getLogger(TranslationExample.class);

  private TranslationExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {
    System.setProperty("ai.djl.default_engine", "PaddlePaddle");
    // 分词
    Jieba jieba = new Jieba();
    // 翻译
    Translation senta = new Translation();
    Criteria<String[], String[]> SentaCriteria = senta.criteria();

    try (ZooModel<String[], String[]> sentaModel = SentaCriteria.loadModel();
         Predictor<String[], String[]> sentaPredictor = sentaModel.newPredictor()) {

      String input = "两条狗在雪地里";
      logger.info("input Sentence: {}", input);

      // 分词
      String[] lacResult = jieba.cut(input);
      logger.info("Jieba分词 : " + Arrays.toString(lacResult));
      // 翻译结果
      String[] translationResult = sentaPredictor.predict(lacResult);
      logger.info("翻译结果: {}", Arrays.toString(translationResult));

    }
  }
}
