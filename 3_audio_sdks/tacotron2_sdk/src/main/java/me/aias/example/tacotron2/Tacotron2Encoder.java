package me.aias.example.tacotron2;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
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

public class Tacotron2Encoder {
  public Tacotron2Encoder() {}

  public Criteria<NDList, NDArray> criteria() {
    Criteria<NDList, NDArray> criteria =
            Criteria.builder()
                    .setTypes(NDList.class, NDArray.class)
                    .optModelPath(Paths.get("models/tacotron2.zip"))
                    .optTranslator(new TacotronTranslator())
                    .optEngine("PyTorch") // Use PyTorch engine
                    .optDevice(Device.cpu())
                    .optProgress(new ProgressBar())
                    .build();

    return criteria;
  }
}
