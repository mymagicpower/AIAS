package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://www.paddlepaddle.org.cn/hubdetail?name=porn_detection_lstm&en_category=TextCensorship
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class ReviewDetection {

  private static final Logger logger = LoggerFactory.getLogger(ReviewDetection.class);

  public ReviewDetection() {}

  public Criteria<String, float[]> criteria() {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelPath(Paths.get("models/review_detection_lstm.zip"))
            .optTranslator(new ReviewTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
