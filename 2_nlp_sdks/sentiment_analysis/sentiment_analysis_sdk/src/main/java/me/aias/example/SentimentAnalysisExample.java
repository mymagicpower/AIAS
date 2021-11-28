package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.SentimentAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class SentimentAnalysisExample {

  private static final Logger logger = LoggerFactory.getLogger(SentimentAnalysisExample.class);

  private SentimentAnalysisExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {

    SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
    Criteria<String, Classifications> criteria = sentimentAnalysis.criteria();

    try (ZooModel<String, Classifications> model = criteria.loadModel();
        Predictor<String, Classifications> predictor = model.newPredictor()) {

      String input = "I like AIAS. AIAS is the best DL application suite!";
      logger.info("input Sentence: {}", input);

      Classifications classifications = predictor.predict(input);
      logger.info(classifications.toString());
    }
  }
}
