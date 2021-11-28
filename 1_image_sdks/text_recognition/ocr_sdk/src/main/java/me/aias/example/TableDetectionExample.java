package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.TableDetection;
import me.aias.example.utils.TableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格检测.
 *
 * @author Calvin
 * @date 2021-10-05
 * @email 179209347@qq.com
 */
public final class TableDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(TableDetectionExample.class);

  private TableDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/table.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    TableDetection detection = new TableDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.criteria());
        Predictor<Image, TableResult> detector = detectionModel.newPredictor()) {

      TableResult result = detector.predict(image);

      List<BoundingBox> boxes = result.getBoxes();
      List<String> names = new ArrayList<>();
      List<Double> probs = new ArrayList<>();
      for (int i = 0; i < boxes.size(); i++) {
        names.add("" + i);
        probs.add(-1.0);
        Rectangle rect = boxes.get(i).getBounds();
      }

      DetectedObjects detections = new DetectedObjects(names, probs, boxes);

      ImageUtils.saveBoundingBoxImage(image, detections, "table_detect_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
