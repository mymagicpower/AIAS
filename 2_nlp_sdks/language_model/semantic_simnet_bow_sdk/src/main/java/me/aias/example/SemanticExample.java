package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.Lac;
import me.aias.example.utils.SimnetBow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 计算短文本相似度
 * Compute short text similarity
 *
 * @author calvin
 * @mail 179209347@qq.com
 */

public final class SemanticExample {

  private static final Logger logger = LoggerFactory.getLogger(SemanticExample.class);

  private SemanticExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {

    // 分词 - Tokenization
    Lac lac = new Lac();
    Criteria<String, String[][]> lacCriteria = lac.criteria();
    // 计算短文本相似度
    // Compute short text similarity
    SimnetBow simnetBow = new SimnetBow();
    Criteria<String[][], Float> simnetBowCriteria = simnetBow.criteria();

    try (ZooModel<String, String[][]> lacModel = lacCriteria.loadModel();
        Predictor<String, String[][]> lacPredictor = lacModel.newPredictor();
        ZooModel<String[][], Float> simnetBowModel = simnetBowCriteria.loadModel();
        Predictor<String[][], Float> simnetBowPredictor = simnetBowModel.newPredictor()) {

      String input1 = "这个棋局太难了";
      String input2 = "这个棋局不简单";
      String input3 = "这个棋局很有意思";

      logger.info("短文本 1: {}", input1);
      String[][] lacResult1 = lacPredictor.predict(input1);
      // 分词 - Tokenization
      logger.info("Words : " + Arrays.toString(lacResult1[0]));
      logger.info("Tags : " + Arrays.toString(lacResult1[1]));
      String[][] lacResult2 = lacPredictor.predict(input2);

      logger.info("短文本 2: {}", input2);
      // 分词 - Tokenization
      logger.info("Words : " + Arrays.toString(lacResult2[0]));
      logger.info("Tags : " + Arrays.toString(lacResult2[1]));
      String[][] lacResult3 = lacPredictor.predict(input3);

      logger.info("短文本 3: {}", input3);
      // 分词 - Tokenization
      logger.info("Words : " + Arrays.toString(lacResult3[0]));
      logger.info("Tags : " + Arrays.toString(lacResult3[1]));

      // 计算短文本相似度 - Compute short text similarity
      int maxLength = Math.max(lacResult1[0].length, lacResult2[0].length);
      String[][] inputs = new String[2][maxLength];
      inputs[0] = lacResult1[0];
      inputs[1] = lacResult2[0];
      Float simResult1 = simnetBowPredictor.predict(inputs);
      logger.info("短文本 - Short text 1: {}", input1);
      logger.info("短文本 - Short text 2: {}", input2);
      logger.info("相似度 - Similarity : " + simResult1.floatValue());

      maxLength = Math.max(lacResult1[0].length, lacResult3[0].length);
      inputs = new String[2][maxLength];
      inputs[0] = lacResult1[0];
      inputs[1] = lacResult3[0];
      Float simResult2 = simnetBowPredictor.predict(inputs);
      logger.info("短文本 - Short text 1: {}", input1);
      logger.info("短文本 - Short text 3: {}", input3);
      logger.info("相似度 - Similarity : " + simResult2.floatValue());
    }
  }
}
