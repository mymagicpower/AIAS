package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

// https://www.sbert.net/docs/pretrained_cross-encoders.html
// https://www.sbert.net/docs/pretrained-models/ce-msmarco.html
public final class TinyBertCrossEncoder {

  public TinyBertCrossEncoder() {}

  public Criteria<String[], Float> criteria() {

    Criteria<String[], Float> criteria =
        Criteria.builder()
            .setTypes(String[].class, Float.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/sentence_transformers/ms-marco-TinyBERT-L-2-v2.zip")
            //            .optModelUrls("/Users/calvin/ms-marco-TinyBERT-L-2-v2/")
            .optTranslator(new CrossEncoderTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
