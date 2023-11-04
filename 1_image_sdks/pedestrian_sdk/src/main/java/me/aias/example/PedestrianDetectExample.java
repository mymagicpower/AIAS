package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.PedestrianDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 行人检测例子
 * Pedestrian Detection
 *
 * @author Calvin
 */

public final class PedestrianDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(PedestrianDetectExample.class);

  private PedestrianDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ped.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, DetectedObjects> criteria = new PedestrianDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
         Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(image);
      ImageUtils.saveBoundingBoxImage(image, detections, "result.png", "build/output");

      logger.info("{}", detections);
    }
  }
}
