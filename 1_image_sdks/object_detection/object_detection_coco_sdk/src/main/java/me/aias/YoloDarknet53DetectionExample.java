package me.aias;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.aias.util.ImageUtils;
import me.aias.util.YoloDarknet53Detection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class YoloDarknet53DetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(YoloDarknet53DetectionExample.class);

  private YoloDarknet53DetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/detection.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    YoloDarknet53Detection detection = new YoloDarknet53Detection();

    //阈值
    double threshold = 0.4;
    DetectedObjects detections = detection.predict(image, threshold);
//    List<DetectedObjects.DetectedObject> items = detections.items();
    //    List<String> names = new ArrayList<>();
    //    List<Double> prob = new ArrayList<>();
    //    List<BoundingBox> rect = new ArrayList<>();

    //    for (DetectedObjects.DetectedObject item : items) {
    //      names.add(item.getClassName());
    //      prob.add(item.getProbability());
    //      rect.add(item.getBoundingBox());
    //    }

    ImageUtils.drawBoundingBoxImage(image, detections);
    ImageUtils.saveImage(image, "YoloDarknet53Detection.png", "build/output");

    logger.info("{}", detections);
  }
}
