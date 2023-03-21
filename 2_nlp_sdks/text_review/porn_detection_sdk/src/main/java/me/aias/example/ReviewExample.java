package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ReviewDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class ReviewExample {

  private static final Logger logger = LoggerFactory.getLogger(ReviewExample.class);

  private ReviewExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {

    ReviewDetection reviewDetection = new ReviewDetection();
    Criteria<String, float[]> SentaCriteria = reviewDetection.criteria();

    try (
        ZooModel<String, float[]> model = SentaCriteria.loadModel();
        Predictor<String, float[]>predictor = model.newPredictor()) {

      String input = "黄色内容";
      logger.info("input Sentence: {}", input);

      // 文本检查 Text check
      float[] result = predictor.predict(input);
      logger.info(Arrays.toString(result));
      logger.info("not_porn : " + result[0]);
      logger.info("porn : " + result[1]);


      input = "黄牛党";
      logger.info("input Sentence: {}", input);

      // 文本检查 Text check
      result = predictor.predict(input);
      logger.info(Arrays.toString(result));
      logger.info("not_porn : " + result[0]);
      logger.info("porn : " + result[1]);
      
    }
  }
}
