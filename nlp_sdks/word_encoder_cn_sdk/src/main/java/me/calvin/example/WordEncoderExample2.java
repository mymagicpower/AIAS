package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import me.calvin.aias.WordEncoder;
import me.calvin.aias.util.FeatureComparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 词向量提取：
 * 词向量的纬度为300，词表大小为195204，训练采用的语料是——Weibo 微博。
 *
 * @author Calvin
 * 179209347@qq.com
 */
public final class WordEncoderExample2 {

  private static final Logger logger = LoggerFactory.getLogger(WordEncoderExample2.class);

  private WordEncoderExample2() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path vocabPath = Paths.get("src/test/resources/w2v_weibo_vocab.txt");
    Path embeddingPath = Paths.get("src/test/resources/w2v_weibo_dim300.npy");

    WordEncoder encoder = new WordEncoder(vocabPath, embeddingPath);

    // 获取单词的特征值embedding
    float[] embedding1 = encoder.search("中国");
    logger.info("中国-特征值: " + Arrays.toString(embedding1));
    float[] embedding2 = encoder.search("美国");
    logger.info("美国-特征值: " + Arrays.toString(embedding1));

    // 计算两个词向量的余弦相似度
    float cosineSim = FeatureComparison.cosineSim(embedding1, embedding2);
    logger.info("余弦相似度: "+ Float.toString(cosineSim));

    // 计算两个词向量的内积
    float dot = FeatureComparison.dot(embedding1, embedding2);
    logger.info("内积: "+ Float.toString(dot));
  }
}
