package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.SentenceEncoder;
import me.aias.example.utils.SpProcessor;
import me.aias.example.utils.SpTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 * 句向量 支持 50+ 语言
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SentenceEncoderExample {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoderExample.class);

  private SentenceEncoderExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String input1 = "This model generates embeddings for input sentence";
    String input2 = "This model generates embeddings";
    String input3 = "今天天气不错";
    String input4 = "今天风和日丽";

    logger.info("input Sentence1: {}", input1);
    logger.info("input Sentence2: {}", input2);

    logger.info("input Sentence3: {}", input3);
    logger.info("input Sentence4: {}", input4);

    SentenceEncoder sentenceEncoder = new SentenceEncoder();
    URL url =
        new URL(
            "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/sentence_transformers/sentencepiece.bpe.model");
    Path path = Paths.get("build/test/models");
    Path modelFile = path.resolve("sentencepiece.bpe.model");
    if (!Files.exists(modelFile)) {
      Files.createDirectories(path);
      try (InputStream is = url.openStream()) {
        Files.copy(is, modelFile, StandardCopyOption.REPLACE_EXISTING);
      }
    }

    try (SpTokenizer tokenizer = new SpTokenizer(modelFile)) {
      SpProcessor processor = tokenizer.getProcessor();
      try (ZooModel<String, float[]> model =
              ModelZoo.loadModel(sentenceEncoder.criteria(processor));
           Predictor<String, float[]> predictor = model.newPredictor()) {

        float[] embeddings1 = predictor.predict(input1);
        logger.info("Vector dimensions: {}", embeddings1.length);
        logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
        float[] embeddings2 = predictor.predict(input2);
        logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));

        logger.info("英文 Similarity: {}", FeatureComparison.cosineSim(embeddings1, embeddings2));

        float[] embeddings3 = predictor.predict(input3);
        logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings3));
        float[] embeddings4 = predictor.predict(input4);
        logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings4));

        logger.info("中文 Similarity: {}", FeatureComparison.cosineSim(embeddings3, embeddings4));
      }
    }
  }
}
