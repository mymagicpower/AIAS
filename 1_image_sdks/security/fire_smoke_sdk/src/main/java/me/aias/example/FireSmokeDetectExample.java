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
import me.aias.example.utils.FireSmokeDetect;
import me.aias.example.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 烟火检测例子
 *
 * 目录：http://aias.top/
 *
 * @author Calvin
 */
public final class FireSmokeDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(FireSmokeDetectExample.class);

  private FireSmokeDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/fire_smoke.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, DetectedObjects> criteria = new FireSmokeDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(image);
      List<DetectedObjects.DetectedObject> items = detections.items();

      List<String> names = new ArrayList<>();
      List<Double> prob = new ArrayList<>();
      List<BoundingBox> boxes = new ArrayList<>();
      for (int i = 0; i < items.size(); i++) {
        DetectedObjects.DetectedObject item = items.get(i);
        if (item.getProbability() < 0.3f) {
          continue;
        }
        names.add(item.getClassName() + " " + item.getProbability());
        prob.add(item.getProbability());
        boxes.add(item.getBoundingBox());
      }

      detections = new DetectedObjects(names, prob, boxes);
      ImageUtils.saveBoundingBoxImage(image, detections, "fire_detect_result.png", "build/output");

      logger.info("{}", detections);
    }
  }
}
