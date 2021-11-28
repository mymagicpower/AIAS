package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public class Tacotron {
  public Tacotron() {}

  public Criteria<NDArray, NDArray> criteria() {

    Criteria<NDArray, NDArray> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, NDArray.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/tacotronSTFT.zip")
            //            .optModelUrls("/Users/calvin/ms-marco-TinyBERT-L-2-v2/")
            // .optModelPath(Paths.get("src/main/resources/voice/tacotronSTFT.pt"))
            .optTranslator(new TacotronTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
