package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.TinyBertCrossEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * MS MARCO Re-Ranker: Cross-Encoder
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TinyBertCrossEncoderExample {

  private static final Logger logger = LoggerFactory.getLogger(TinyBertCrossEncoderExample.class);

  private TinyBertCrossEncoderExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String[] input1 = {
      "How many people live in Berlin?",
      "Berlin had a population of 3,520,031 registered inhabitants in an area of 891.82 square kilometers."
    };
    String[] input2 = {"How many people live in Berlin?", "Berlin is well known for its museums."};

    logger.info("input query1: {}", Arrays.toString(input1));
    logger.info("input query2: {}", Arrays.toString(input2));

    TinyBertCrossEncoder sentenceEncoder = new TinyBertCrossEncoder();
    try (ZooModel<String[], Float> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String[], Float> predictor = model.newPredictor()) {

      Float score1 = predictor.predict(input1);
      logger.info("Score1: {}", score1.floatValue());
      Float score2 = predictor.predict(input2);
      logger.info("Score2: {}", score2.floatValue());
    }
  }
}
