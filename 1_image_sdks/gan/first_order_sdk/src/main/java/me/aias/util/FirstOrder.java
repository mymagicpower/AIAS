package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

import java.util.List;
import java.util.Map;

public class FirstOrder {

  public FirstOrder() {}

  public Criteria<Image, Map> detector() {
    Criteria<Image, Map> kpDetector =
        Criteria.builder()
            .setTypes(Image.class, Map.class)
            .optTranslator(new NPtKTranslator())
            .optEngine("PyTorch")
            .optProgress(new ProgressBar())
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/gan_models/kpdetector.zip")
            // .optModelPath(Paths.get("/Users/calvin/Documents/build/pytorch_models/AI_MODEL/kpdetector"))
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
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/gan_models/generator.zip")
            // .optModelPath(Paths.get("/Users/calvin/Documents/build/pytorch_models/AI_MODEL/generator"))
            .build();
    return generator;
  }
}
