package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.QuestionAnswerRetrieval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 语义搜索
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */

public final class QuestionAnswerRetrievalExample {

  private static final Logger logger = LoggerFactory.getLogger(QuestionAnswerRetrievalExample.class);

  private QuestionAnswerRetrievalExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String query = "How big is London";
    String passage = "London has 9,787,426 inhabitants at the 2011 census";

    logger.info("Query sentence: {}", query);
    logger.info("Passage sentence: {}", passage);

    QuestionAnswerRetrieval sentenceEncoder = new QuestionAnswerRetrieval();
    try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> predictor = model.newPredictor()) {

      float[] queryEmbedding = predictor.predict(query);
      logger.info("Vector dimension: {}", queryEmbedding.length);
      
      logger.info("Query sentence embeddings: {}", Arrays.toString(queryEmbedding));
      float[] passageEmbedding = predictor.predict(passage);
      logger.info("Passage embeddings: {}", Arrays.toString(passageEmbedding));

      logger.info("Similarity: {}", FeatureComparison.cosineSim(queryEmbedding,passageEmbedding));
    }
  }
}
