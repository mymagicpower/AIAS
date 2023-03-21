package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public class FirstOrder {

  public FirstOrder() {}

  public Criteria<Image, Map> detector() {
    Criteria<Image, Map> kpDetector =
        Criteria.builder()
            .setTypes(Image.class, Map.class)
            .optTranslator(new NPtKTranslator())
            .optEngine("PyTorch")
            .optProgress(new ProgressBar())
            .optModelPath(Paths.get("models/kpdetector.zip"))
            .build();
    return kpDetector;
  }

  public Criteria<List, Image> generator() {
    Criteria<List, Image> generator =
        Criteria.builder()
            .setTypes(List.class, Image.class)
            .optEngine("PyTorch")
            .optTranslator(new PtGTranslator())
            .optProgress(new ProgressBar())
            .optModelPath(Paths.get("models/generator.zip"))
            .build();
    return generator;
  }
}
