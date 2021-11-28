package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import me.aias.TextTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TextEncoder {

  private static final Logger logger = LoggerFactory.getLogger(TextEncoder.class);

  public TextEncoder() {}

  public Criteria<String, float[]> criteria() {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/clip_series/CLIP-ViT-B-32-TEXT.zip")
//            .optModelUrls("/Users/calvin/CLIP-ViT-B-32-TEXT/")
            .optTranslator(new TextTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
