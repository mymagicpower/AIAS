package me.aias.example;

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.FfmpegUtils;
import me.aias.example.utils.Tacotron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * TacotronSTFT 提取mel频谱
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TacotronSTFTExample {

  private static final Logger logger =
      LoggerFactory.getLogger(TacotronSTFTExample.class);

  private TacotronSTFTExample() {}

  public static void main(String[] args) throws Exception {
    Path audioFile = Paths.get("src/test/resources/biaobei-009502.mp3");

    // 使用ffmpeg 将mp3文件转为wav格式
    NDArray audioArray = FfmpegUtils.load_wav_to_torch(audioFile.toString(), 22050);
    Tacotron tacotron = new Tacotron();

    try (ZooModel<NDArray, NDArray> model = ModelZoo.loadModel(tacotron.criteria());
        Predictor<NDArray, NDArray> stftPredictor = model.newPredictor()) {

      NDArray melspec = stftPredictor.predict(audioArray);
      Shape shape = melspec.getShape();
      logger.info("melspec shape: {}", Arrays.toString(shape.getShape()));

      for (int i = 0; i < shape.get(0); i++) {
        logger.info("Row {}: {}", i, Arrays.toString(melspec.get(i).toFloatArray()));
      }
    }
  }
}
