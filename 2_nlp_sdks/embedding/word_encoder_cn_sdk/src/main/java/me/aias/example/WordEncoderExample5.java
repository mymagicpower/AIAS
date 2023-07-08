package me.aias.example;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.WordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class WordEncoderExample5 {

  private static final Logger logger = LoggerFactory.getLogger(WordEncoderExample5.class);

  private WordEncoderExample5() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path vocabPath = Paths.get("src/test/resources/w2v_literature_dim300_vocab.txt");
    Path embeddingPath = Paths.get("src/test/resources/w2v_literature_dim300.npy");

    WordEncoder encoder = new WordEncoder(vocabPath, embeddingPath);

    // 获取单词的特征值embedding
    float[] embedding1 = encoder.search("玄幻");
    logger.info("玄幻-特征值: " + Arrays.toString(embedding1));
    float[] embedding2 = encoder.search("科幻");
    logger.info("科幻-特征值: " + Arrays.toString(embedding1));

    // 计算两个词向量的余弦相似度
    float cosineSim = FeatureComparison.cosineSim(embedding1, embedding2);
    logger.info("余弦相似度: "+ Float.toString(cosineSim));

    // 计算两个词向量的内积
    float dot = FeatureComparison.dot(embedding1, embedding2);
    logger.info("内积: "+ Float.toString(dot));
  }
}
