package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.paddlepaddle.org.cn/hubdetail?name=lac&en_category=LexicalAnalysis
public final class Lac {

  private static final Logger logger = LoggerFactory.getLogger(Lac.class);

  public Lac() {}

  public Criteria<String, String[][]> criteria() {
    Criteria<String, String[][]> criteria =
        Criteria.builder()
            .setTypes(String.class, String[][].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/lac.zip")
            //            .optModelUrls("/Users/calvin/model/lac/")
            .optTranslator(new LacTranslator())
            .optProgress(new ProgressBar())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            // .optDevice(Device.cpu())
            .build();

    return criteria;
  }
}
