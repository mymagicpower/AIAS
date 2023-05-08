package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.cls.OcrDirectionDetection;
import me.aias.example.common.DirectionInfo;
import me.aias.example.common.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR文字方向检测(轻量级模型).
 *
 * OCR text direction detection (light model)
 *
 * @author Calvin
 * @date 2021-10-04
 * @email 179209347@qq.com
 */
public final class OcrDirectionExample {

  private static final Logger logger = LoggerFactory.getLogger(OcrDirectionExample.class);

  private OcrDirectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_90.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    OcrDirectionDetection detection = new OcrDirectionDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
         Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
         ZooModel rotateModel = ModelZoo.loadModel(detection.clsCriteria());
         Predictor<Image, DirectionInfo> rotateClassifier = rotateModel.newPredictor()) {

      DetectedObjects detections = detection.predict(image,detector,rotateClassifier);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(image, detections, "cls_detect_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}