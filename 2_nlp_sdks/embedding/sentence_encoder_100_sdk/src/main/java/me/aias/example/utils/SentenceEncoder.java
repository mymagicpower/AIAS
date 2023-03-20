package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SentenceEncoder {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoder.class);

  public SentenceEncoder() {}

  public Criteria<String, float[]> criteria(SpProcessor processor) {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelPath(Paths.get("models/paraphrase-xlm-r-multilingual-v1.zip"))
            .optTranslator(new SentenceTransTranslator(processor))
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
