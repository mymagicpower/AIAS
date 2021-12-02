package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public class Voiceprint {
  public Voiceprint() {}

  public Criteria<float[][], float[]> criteria() {
    Criteria<float[][], float[]> criteria =
        Criteria.builder()
            .setTypes(float[][].class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/voiceprint.zip")
            // .optModelUrls(Paths.get("src/main/resources/voice/"))
            .optTranslator(new VoiceprintTranslator())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
