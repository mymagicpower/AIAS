package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.LightOcrDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR文字检测(轻量级模型).
 *
 * @author Calvin
 * @date 2021-10-04
 * @email 179209347@qq.com
 */
public final class LightOcrDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(LightOcrDetectionExample.class);

  private LightOcrDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_270.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    LightOcrDetection detection = new LightOcrDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
         Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
         ZooModel rotateModel = ModelZoo.loadModel(detection.clsCriteria());
         Predictor<Image, Classifications> rotateClassifier = rotateModel.newPredictor()) {

      DetectedObjects detections = detection.predict(image,detector,rotateClassifier);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(image, detections, "light_detect_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
