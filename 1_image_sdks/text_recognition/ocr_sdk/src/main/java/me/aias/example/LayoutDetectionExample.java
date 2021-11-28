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
import me.aias.example.utils.LayoutDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 布局检测.
 *
 * @author Calvin
 * @date 2021-10-04
 * @email 179209347@qq.com
 */
public final class LayoutDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(LayoutDetectionExample.class);

  private LayoutDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/layout.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    LayoutDetection detection = new LayoutDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.criteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor()) {

      DetectedObjects detections = detector.predict(image);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(
          image, detections, "layout_detect_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
