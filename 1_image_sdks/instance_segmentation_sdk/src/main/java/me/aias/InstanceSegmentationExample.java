package me.aias;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.aias.util.ImageUtils;
import me.aias.util.InstanceSegmentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class InstanceSegmentationExample {

  private static final Logger logger = LoggerFactory.getLogger(InstanceSegmentationExample.class);

  private InstanceSegmentationExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/detection.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    InstanceSegmentation detection = new InstanceSegmentation();

    DetectedObjects detections = detection.predict(image);
    List<DetectedObjects.DetectedObject> items = detections.items();
    List<String> names = new ArrayList<>();
    List<Double> prob = new ArrayList<>();
    List<BoundingBox> rect = new ArrayList<>();

    for (DetectedObjects.DetectedObject item : items) {
      if (item.getProbability() < 0.55) continue;
      names.add(item.getClassName());
      prob.add(item.getProbability());
      rect.add(item.getBoundingBox());
    }
    DetectedObjects detectionsFiltered = new DetectedObjects(names, prob, rect);

    ImageUtils.drawBoundingBoxImage(image, detectionsFiltered);
    ImageUtils.saveImage(image, "result.png", "build/output");

    logger.info("{}", detections);
  }
}
