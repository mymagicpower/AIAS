package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.SemanticSearchPublications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public final class SemanticSearchPublicationsExample {

  private static final Logger logger = LoggerFactory.getLogger(SemanticSearchPublicationsExample.class);

  private SemanticSearchPublicationsExample() {}

  /**
   * 学术文章语义搜索
   *
   * @author calvin
   * @mail 179209347@qq.com
   * @website www.aias.top
   */
  public static void main(String[] args) throws IOException, ModelException, TranslateException {

    //传入的参数为文章[标题,摘要] [title, abstract]
    // subword切词，合计最大长度 max_sequence_length: 256, 按经验上限平均130个单词左右
    String[] paper1 = {"BERT", "We introduce a new language representation model called BERT"};
    String[] paper2 = {"Attention is all you need", "The dominant sequence transduction models are based on complex recurrent or convolutional neural networks"};
    logger.info("paper1 [title, abstract]: {}", Arrays.toString(paper1));
    logger.info("paper2 [title, abstract]: {}", Arrays.toString(paper2));

    SemanticSearchPublications sentenceEncoder = new SemanticSearchPublications();
    try (ZooModel<String[], float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String[], float[]> predictor = model.newPredictor()) {

      float[] paper1Embeddings = predictor.predict(paper1);
      logger.info("Vector dimension: {}", paper1Embeddings.length);
      
      logger.info("paper1[title, text] embeddings: {}", Arrays.toString(paper1Embeddings));
      float[] paper2Embeddings = predictor.predict(paper2);
      logger.info("paper2[title, text] embeddings: {}", Arrays.toString(paper2Embeddings));

      logger.info("Similarity: {}", FeatureComparison.cosineSim(paper1Embeddings,paper2Embeddings));
    }
  }
}
