package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://www.paddlepaddle.org.cn/hubdetail?name=emotion_detection_textcnn&en_category=SentimentAnalysis
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class SentaTextCnn {

  private static final Logger logger = LoggerFactory.getLogger(SentaTextCnn.class);

  public SentaTextCnn() {}

  public Criteria<String[], float[]> criteria() {

    Criteria<String[], float[]> criteria =
        Criteria.builder()
            .setTypes(String[].class, float[].class)
            .optModelPath(Paths.get("models/senta_textcnn.zip"))
            .optTranslator(new SentaTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
