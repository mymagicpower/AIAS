package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://www.paddlepaddle.org.cn/hubdetail?name=lac&en_category=LexicalAnalysis
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class Lac {

  private static final Logger logger = LoggerFactory.getLogger(Lac.class);

  public Lac() {}

  public Criteria<String, String[][]> criteria() {
    Criteria<String, String[][]> criteria =
        Criteria.builder()
            .setTypes(String.class, String[][].class)
            .optModelPath(Paths.get("models/lac.zip"))
            .optTranslator(new LacTranslator())
            .optProgress(new ProgressBar())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            .build();

    return criteria;
  }
}
