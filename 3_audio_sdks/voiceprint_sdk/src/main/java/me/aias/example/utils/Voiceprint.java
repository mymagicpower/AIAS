package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class Voiceprint {
  public Voiceprint() {}

  public Criteria<float[][], float[]> criteria() {
    Criteria<float[][], float[]> criteria =
        Criteria.builder()
            .setTypes(float[][].class, float[].class)
            .optModelPath(Paths.get("models/voiceprint.zip"))
            .optTranslator(new VoiceprintTranslator())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
