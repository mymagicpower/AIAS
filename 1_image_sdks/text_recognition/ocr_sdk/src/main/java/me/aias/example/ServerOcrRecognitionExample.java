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
import me.aias.example.utils.ServerOcrRecognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 轻量级OCR文字识别.
 *
 * @author Calvin
 * @date 2021-10-07
 * @email 179209347@qq.com
 */
public final class ServerOcrRecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(ServerOcrRecognitionExample.class);

  private ServerOcrRecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_0.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    ServerOcrRecognition recognition = new ServerOcrRecognition();
    try (ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
        ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
        Predictor<Image, String> recognizer = recognitionModel.newPredictor()) {

      DetectedObjects detections = recognition.predict(image, detector, recognizer);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(image, detections, "server_ocr_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
