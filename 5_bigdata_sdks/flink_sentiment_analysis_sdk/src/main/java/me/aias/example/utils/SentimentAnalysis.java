package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.Classifications;
import ai.djl.pytorch.zoo.nlp.sentimentanalysis.PtDistilBertTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SentimentAnalysis {

  private static final Logger logger = LoggerFactory.getLogger(SentimentAnalysis.class);

  public SentimentAnalysis() {}

  public Criteria<String, Classifications> criteria() {
    Criteria<String, Classifications> criteria =
        Criteria.builder()
            .setTypes(String.class, Classifications.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/distilbert_sst_english.zip")
            //  .optModelUrls(
            // "/Users/calvin/Documents/build/pytorch_models/distilbert_sst_english/")
            .optTranslator(new PtDistilBertTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
