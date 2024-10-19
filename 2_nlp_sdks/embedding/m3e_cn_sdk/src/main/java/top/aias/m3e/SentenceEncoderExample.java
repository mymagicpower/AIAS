package top.aias.m3e;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import top.aias.m3e.utils.FeatureComparison;
import top.aias.m3e.utils.SentenceEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 句向量 支持中文及英文（中文为主的场景）
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SentenceEncoderExample {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoderExample.class);

  private SentenceEncoderExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String input1 = "今天天气不错";
    String input2 = "今天风和日丽";

    logger.info("input Sentence1: {}", input1);
    logger.info("input Sentence2: {}", input2);


    SentenceEncoder sentenceEncoder = new SentenceEncoder();
    try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> predictor = model.newPredictor()) {

      float[] embeddings1 = predictor.predict(input1);
      logger.info("Vector dimensions: {}", embeddings1.length);
      logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
      float[] embeddings2 = predictor.predict(input2);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));

      logger.info("Chinese Similarity: {}", FeatureComparison.cosineSim(embeddings1, embeddings2));
    }
  }
}
