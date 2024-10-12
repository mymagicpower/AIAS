package me.aias.example.speaker;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
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

public class SpeakerEncoder {
  public SpeakerEncoder() {}

  public Criteria<NDArray, NDArray> criteria() {
    Criteria<NDArray, NDArray> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, NDArray.class)
            .optModelPath(Paths.get("models/speakerEncoder.zip"))
            .optTranslator(new SpeakerEncoderTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
