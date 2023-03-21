package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.Lac;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
/**
 * 中文切词 LAC
 * Chinese Tokenization - LAC
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class LacExample {

  private static final Logger logger = LoggerFactory.getLogger(LacExample.class);

  private LacExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {

    Lac lac = new Lac();
    Criteria<String, String[][]> criteria = lac.criteria();

    try (ZooModel<String, String[][]> model = criteria.loadModel();
         Predictor<String, String[][]> predictor = model.newPredictor()) {

      String input = "今天是个好日子";
      
      logger.info("input Sentence: {}", input);

      String[][] result = predictor.predict(input);
      logger.info("Words : " + Arrays.toString(result[0]));
      logger.info("Tags : " + Arrays.toString(result[1]));

      
    }
  }
}
