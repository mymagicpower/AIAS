package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.SentenceEncoderEN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public final class SentenceEncoderExample {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoderExample.class);

  private SentenceEncoderExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String input1 = "This model generates embeddings for input sentence";
    String input2 = "This model generates embeddings";
    logger.info("input Sentence1: {}", input1);
    logger.info("input Sentence2: {}", input2);

    SentenceEncoderEN sentenceEncoder = new SentenceEncoderEN();
    try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> predictor = model.newPredictor()) {

      float[] embeddings1 = predictor.predict(input1);
      logger.info("Vector dimensions: {}",embeddings1.length);
      
      logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
      float[] embeddings2 = predictor.predict(input2);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));

      logger.info("Similarity: {}", FeatureComparison.cosineSim(embeddings1,embeddings2));
    }
  }
}
