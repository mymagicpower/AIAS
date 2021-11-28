package me.aias.example;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.utils.AudioProcess;
import me.aias.example.utils.AudioUtils;
import me.aias.example.utils.AudioVadUtils;
import me.aias.example.utils.SpeechRecognition;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;

/**
 * 预测长语音
 *
 * <p>https://github.com/yeyupiaoling/PaddlePaddle-DeepSpeech
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SpeechRecognitionExampleL {
  private static final Logger logger = LoggerFactory.getLogger(SpeechRecognitionExampleL.class);

  public static void main(String[] args) throws Exception {
    String os = System.getProperty("os.name");
    if (os.contains("Windows")) {
      System.out.println(
          "To support windows, please refer to https://github.com/mymagicpower/AIAS/tree/main/speech_sdks/vad4j_sdk");
    }

    Path path = Paths.get("src/test/resources/test.wav");

    NDManager manager = NDManager.newBaseManager(Device.cpu());
    Queue<byte[]> segments = AudioVadUtils.cropAudioVad(path, 300, 30);

    SpeechRecognition speakerEncoder = new SpeechRecognition();
    Criteria<NDArray, Pair> criteria = speakerEncoder.criteria();

    try (ZooModel<NDArray, Pair> model = criteria.loadModel();
        Predictor<NDArray, Pair> predictor = model.newPredictor()) {
      logger.info("input audio: {}", "src/test/resources/test.wav");
      int index = 1;
      String texts = "";
      for (byte[] que : segments) {
        NDArray array = AudioUtils.bytesToFloatArray(manager, que);
        NDArray audioFeature = AudioProcess.processUtterance(manager, array);
        Pair result = predictor.predict(audioFeature);
        texts = texts + "," + result.getRight();
        logger.info("第{}个分割音频, 得分: {}, 识别结果: {}", index++, result.getLeft(), result.getRight());
      }

      logger.info("最终识别结果:" + texts);
    }
  }
}
