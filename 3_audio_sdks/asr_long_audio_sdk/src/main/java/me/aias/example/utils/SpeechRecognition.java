package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.apache.commons.lang3.tuple.Pair;

public class SpeechRecognition {
  public SpeechRecognition() {}

  public Criteria<NDArray, Pair> criteria() {
    Criteria<NDArray, Pair> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, Pair.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/deep_speech.zip")
//            .optModelUrls("/Users/calvin/aias_projects/PaddlePaddle-DeepSpeech/models/infer/")
            // .optModelPath(Paths.get("src/main/resources/voice/tacotronSTFT.pt"))
            .optTranslator(new AudioTranslator())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
