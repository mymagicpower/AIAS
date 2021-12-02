package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.paddlepaddle.org.cn/hubdetail?name=senta_lstm&en_category=SentimentAnalysis

public final class SentaLstm {

  private static final Logger logger = LoggerFactory.getLogger(SentaLstm.class);

  public SentaLstm() {}

  public Criteria<String[], float[]> criteria() {

    Criteria<String[], float[]> criteria =
        Criteria.builder()
            .setTypes(String[].class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/senta_lstm.zip")
            //            .optModelUrls("/Users/calvin/model/senta_lstm/")
            .optTranslator(new SentaTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
