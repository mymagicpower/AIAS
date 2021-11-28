package me.aias.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.TrafficDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class TrafficDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(TrafficDetectionExample.class);

  private TrafficDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ped_vec.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    DetectedObjects detections = TrafficDetection.predict(image);

    List<DetectedObjects.DetectedObject> items = detections.items();
    List<String> names = new ArrayList<>();
    List<Double> probs = new ArrayList<>();
    List<BoundingBox> boxes = new ArrayList<>();
    for (DetectedObjects.DetectedObject item : items) {
      names.add(item.getClassName());
      probs.add(item.getProbability());
      boxes.add(item.getBoundingBox());
    }

    saveBoundingBoxImage(image, detections, "result2.png", "build/output");

    logger.info("{}", detections);
  }

  private static void saveBoundingBoxImage(
      Image img, DetectedObjects detection, String name, String path) throws IOException {
    // Make image copy with alpha channel because original image was jpg
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK can't save jpg with alpha channel
    img.save(Files.newOutputStream(imagePath), "png");
  }
}
