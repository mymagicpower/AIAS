package me.aias.example.tacotron2;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public class Tacotron2Encoder {
  public Tacotron2Encoder() {}

  public Criteria<NDList, NDArray> criteria() {
    Criteria<NDList, NDArray> criteria =
            Criteria.builder()
                    .setTypes(NDList.class, NDArray.class)
                    .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/tacotron2.zip")
                    .optTranslator(new TacotronTranslator())
                    .optEngine("PyTorch") // Use PyTorch engine
                    // This model was traced on CPU and can only run on CPU
                    .optDevice(Device.cpu())
                    .optProgress(new ProgressBar())
                    .build();

    return criteria;
  }
}
