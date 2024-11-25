package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://www.paddlepaddle.org.cn/hubdetail?name=simnet_bow&en_category=SemanticModel
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class SimnetBow {

  private static final Logger logger = LoggerFactory.getLogger(SimnetBow.class);

  public SimnetBow() {}

  public Criteria<String[][], Float> criteria() {

    Criteria<String[][], Float> criteria =
        Criteria.builder()
            .setTypes(String[][].class, Float.class)
            .optModelPath(Paths.get("models/simnet_bow.zip"))
            .optTranslator(new SimnetBowTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
