package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public final class TextEncoder {

  public TextEncoder() {}

  public Criteria<String, float[]> criteria(boolean isChinese) {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/clip_series/M-BERT-Base-ViT-B.zip")
            // .optModelUrls("/Users/calvin/M-BERT-Base-ViT-B/")
            .optTranslator(new TextTranslator(isChinese))
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
