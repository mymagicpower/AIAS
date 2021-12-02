package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.paddlepaddle.org.cn/hubdetail?name=simnet_bow&en_category=SemanticModel

public final class SimnetBow {

  private static final Logger logger = LoggerFactory.getLogger(SimnetBow.class);

  public SimnetBow() {}

  public Criteria<String[][], Float> criteria() {

    Criteria<String[][], Float> criteria =
        Criteria.builder()
            .setTypes(String[][].class, Float.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/semantic/simnet_bow.zip")
            //            .optModelUrls("/Users/calvin/model/simnet_bow/")
            .optTranslator(new SimnetBowTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
