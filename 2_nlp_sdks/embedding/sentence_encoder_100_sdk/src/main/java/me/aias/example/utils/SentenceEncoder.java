package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SentenceEncoder {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoder.class);

  public SentenceEncoder() {}

  public Criteria<String, float[]> criteria(SpProcessor processor) {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/sentence_transformers/paraphrase-xlm-r-multilingual-v1.zip")
            //            .optModelUrls("/Users/calvin/models/paraphrase-xlm-r-multilingual-v1/")
            .optTranslator(new SentenceTransTranslator(processor))
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
