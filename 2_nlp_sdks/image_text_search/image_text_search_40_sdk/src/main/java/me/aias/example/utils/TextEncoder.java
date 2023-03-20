package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TextEncoder {

  public TextEncoder() {}

  public Criteria<String, float[]> criteria(boolean isChinese) {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelPath(Paths.get("models/M-BERT-Base-ViT-B.zip"))
            .optTranslator(new TextTranslator(isChinese))
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
