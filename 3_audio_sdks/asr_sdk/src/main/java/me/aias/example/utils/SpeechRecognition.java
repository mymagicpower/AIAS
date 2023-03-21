package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.apache.commons.lang3.tuple.Pair;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class SpeechRecognition {
  public SpeechRecognition() {}

  public Criteria<NDArray, Pair> criteria() {
    Criteria<NDArray, Pair> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, Pair.class)
            .optModelPath(Paths.get("models/deep_speech.zip"))
            .optTranslator(new AudioTranslator())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
