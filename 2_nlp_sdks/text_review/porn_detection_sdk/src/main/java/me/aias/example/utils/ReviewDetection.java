package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.paddlepaddle.org.cn/hubdetail?name=porn_detection_lstm&en_category=TextCensorship

public final class ReviewDetection {

  private static final Logger logger = LoggerFactory.getLogger(ReviewDetection.class);

  public ReviewDetection() {}

  public Criteria<String, float[]> criteria() {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/review_detection_lstm.zip")
            //            .optModelUrls("/Users/calvin/model/review_detection_lstm/")
            .optTranslator(new ReviewTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
