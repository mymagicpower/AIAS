package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.MobileOcrDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * OCR文字检测.
 *
 * @author Calvin
 * @date 2021-10-04
 * @email 179209347@qq.com
 */
public final class OcrDetectionHelperExample {

  private static final Logger logger = LoggerFactory.getLogger(OcrDetectionHelperExample.class);

  private OcrDetectionHelperExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_0.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    MobileOcrDetection detection = new MobileOcrDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
        ZooModel rotateModel = ModelZoo.loadModel(detection.clsCriteria());
        Predictor<Image, Classifications> rotateClassifier = rotateModel.newPredictor()) {

      DetectedObjects detections = detection.predict(image, detector, rotateClassifier);

      List<DetectedObjects.DetectedObject> boxes = detections.items();

      List<String> names = new ArrayList<>();
      List<Double> prob = new ArrayList<>();
      List<BoundingBox> rect = new ArrayList<>();
      for (int i = 0; i < boxes.size(); i++) {
        names.add(boxes.get(i).getClassName() + " :" + boxes.get(i).getProbability());
        System.out.println(boxes.get(i).getClassName() + " :" + boxes.get(i).getProbability());
        prob.add(boxes.get(i).getProbability());
        rect.add(boxes.get(i).getBoundingBox());
      }
      DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);
      ImageUtils.saveBoundingBoxImage(
          image, detectedObjects, "mobile_detect_result.png", "build/output");
      logger.info("{}", detectedObjects);
    }
  }
}
