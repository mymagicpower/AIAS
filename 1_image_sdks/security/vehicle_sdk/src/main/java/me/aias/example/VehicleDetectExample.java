package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.VehicleDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆检测例子
 * Vehicle Detection
 *
 * @author Calvin
 */
public final class VehicleDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(VehicleDetectExample.class);

  private VehicleDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/vehicle.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, DetectedObjects> criteria = new VehicleDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(image);

      List<DetectedObjects.DetectedObject> items = detections.items();
      List<String> names = new ArrayList<>();
      List<Double> prob = new ArrayList<>();
      List<BoundingBox> rect = new ArrayList<>();
      for (DetectedObjects.DetectedObject item : items) {
        if (item.getProbability() < 0.55) {
          continue;
        }
        names.add(item.getClassName());
        prob.add(item.getProbability());
        rect.add(item.getBoundingBox());
      }

      detections = new DetectedObjects(names, prob, rect);

      ImageUtils.saveBoundingBoxImage(image, detections, "vehicle_result.png", "build/output");

      logger.info("{}", detections);
    }
  }
}
