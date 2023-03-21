package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.Lac;
import me.aias.example.utils.SentaLstm;
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
public final class SentaLstmExample {

  private static final Logger logger = LoggerFactory.getLogger(SentaLstmExample.class);

  private SentaLstmExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {

    // 分词 - Tokenization
    Lac lac = new Lac();
    Criteria<String, String[][]> lacCriteria = lac.criteria();
    // 情感分析 - Sentiment analysis
    SentaLstm senta = new SentaLstm();
    Criteria<String[], float[]> SentaCriteria = senta.criteria();

    try (ZooModel<String, String[][]> lacModel = lacCriteria.loadModel();
        Predictor<String, String[][]> lacPredictor = lacModel.newPredictor();
        ZooModel<String[], float[]> sentaModel = SentaCriteria.loadModel();
        Predictor<String[], float[]> sentaPredictor = sentaModel.newPredictor()) {

      String input = "这家餐厅很好吃";
      logger.info("input Sentence: {}", input);

      String[][] lacResult = lacPredictor.predict(input);
      // 分词 Tokenization
      logger.info("Words : " + Arrays.toString(lacResult[0]));
      logger.info("Tags : " + Arrays.toString(lacResult[1]));

      // 情感分析 Sentiment analysis
      float[] sentaResult = sentaPredictor.predict(lacResult[0]);
      logger.info(Arrays.toString(sentaResult));
      logger.info("negative : " + sentaResult[0]);
      logger.info("positive : " + sentaResult[1]);
      
    }
  }
}
