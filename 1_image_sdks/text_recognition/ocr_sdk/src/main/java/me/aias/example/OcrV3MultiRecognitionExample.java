package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.OcrV3MultiThreadRecognition;
import me.aias.example.utils.OcrV3Recognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Mobile OCR文字识别.
 *
 * @author Calvin
 * @date 2021-10-07
 * @email 179209347@qq.com
 */
public final class OcrV3MultiRecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(OcrV3MultiRecognitionExample.class);

  private OcrV3MultiRecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_0.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    // 是否启用字符置信度过滤，用于辅助解决重复字符问题
    boolean enableFilter = true;
    // 置信度阈值, 越高重复字符概率越低，但是可能多度过滤字符
    float thresh = 0.5f;
    // 并发线程数，最大上限为 CPU 核数
    int threadNum = 4;

    OcrV3MultiThreadRecognition recognition = new OcrV3MultiThreadRecognition();
    try (ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
        ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria(enableFilter, thresh))) {

      DetectedObjects detections = recognition.predict(image, detector, recognitionModel, threadNum);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(image, detections, "ocr_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
