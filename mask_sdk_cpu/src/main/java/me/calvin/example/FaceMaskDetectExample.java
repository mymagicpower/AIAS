package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.calvin.facemask.FaceMaskDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class FaceMaskDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(FaceMaskDetect.class);

  private FaceMaskDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String facePath = "src/test/resources/masks.png";
    BufferedImage img = ImageIO.read(new File(facePath));
    Image image = ImageFactory.getInstance().fromImage(img);

    DetectedObjects detections = FaceMaskDetect.predict(image);
    List<DetectedObjects.DetectedObject> faces = detections.items();

    //    List<String> names = new ArrayList<>();
    //    List<Double> prob = new ArrayList<>();
    //    List<BoundingBox> rect = new ArrayList<>();
    //    for (DetectedObjects.DetectedObject face : faces) {
    //      prob.add(face.getProbability());
    //      rect.add(face.getBoundingBox());
    //    }

    logger.info("{}", detections);

    saveBoundingBoxImage(image, detections);
  }

  private static void saveBoundingBoxImage(Image img, DetectedObjects detection)
      throws IOException {
    Path outputDir = Paths.get("build/output");
    Files.createDirectories(outputDir);

    // Make image copy with alpha channel because original image was jpg
    Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
    newImage.drawBoundingBoxes(detection);

    Path imagePath = outputDir.resolve("face-masks.png");
    newImage.save(Files.newOutputStream(imagePath), "png");
    logger.info("Face mask detection result image has been saved in: {}", imagePath);
  }
}
