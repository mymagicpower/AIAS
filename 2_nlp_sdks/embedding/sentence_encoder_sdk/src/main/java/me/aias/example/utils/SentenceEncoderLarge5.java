package me.aias.example.utils;

import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SentenceEncoderLarge5 {
  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoderLarge5.class);

  public SentenceEncoderLarge5() {}

  public Criteria<String[], float[][]> criteria() {

    Criteria<String[], float[][]> criteria =
        Criteria.builder()
            //                    .optApplication(Application.NLP.TEXT_EMBEDDING)
            .setTypes(String[].class, float[][].class)
            //            .optOption("tags", "")
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/sentence_encoder_large_5.zip")
            // .optModelUrls(
            // "/Users/calvin/Documents/build/tf_models/universal-sentence_encoder_large_5/")
            .optTranslator(new SentenceEncoderTranslator())
            .optProgress(new ProgressBar())
            .optEngine("TensorFlow") // Use TensorFlow engine
            .build();
    return criteria;
  }
}
