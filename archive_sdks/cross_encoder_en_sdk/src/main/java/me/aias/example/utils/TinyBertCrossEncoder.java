package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://www.sbert.net/docs/pretrained_cross-encoders.html
// https://www.sbert.net/docs/pretrained-models/ce-msmarco.html
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class TinyBertCrossEncoder {

  public TinyBertCrossEncoder() {}

  public Criteria<String[], Float> criteria() {

    Criteria<String[], Float> criteria =
        Criteria.builder()
            .setTypes(String[].class, Float.class)
            .optModelPath(Paths.get("models/ms-marco-TinyBERT-L-2-v2.zip"))
            .optTranslator(new CrossEncoderTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
