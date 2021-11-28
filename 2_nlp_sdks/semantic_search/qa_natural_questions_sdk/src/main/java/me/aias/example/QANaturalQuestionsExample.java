package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.QANaturalQuestions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 自然问题问答
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class QANaturalQuestionsExample {

  private static final Logger logger = LoggerFactory.getLogger(QANaturalQuestionsExample.class);

  private QANaturalQuestionsExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String[] query = {"How many people live in London?"};

    //The passages are encoded as [ [title1, text1], [title2, text2], ...]
    String[] passage = {"London", "London has 9,787,426 inhabitants at the 2011 census."};
    logger.info("query: {}", query);
    logger.info("passage [title, text]: {}", Arrays.toString(passage));

    QANaturalQuestions sentenceEncoder = new QANaturalQuestions();
    try (ZooModel<String[], float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String[], float[]> predictor = model.newPredictor()) {

      float[] queryEmbeddings = predictor.predict(query);
      logger.info("Vector dimension: {}", queryEmbeddings.length);
      
      logger.info("query embeddings: {}", Arrays.toString(queryEmbeddings));
      float[] passageEmbeddings = predictor.predict(passage);
      logger.info("passage[title, text] embeddings: {}", Arrays.toString(passageEmbeddings));

      logger.info("Similarity: {}", FeatureComparison.cosineSim(queryEmbeddings,passageEmbeddings));
    }
  }
}
