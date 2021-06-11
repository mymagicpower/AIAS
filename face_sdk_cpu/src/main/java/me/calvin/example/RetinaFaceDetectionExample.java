package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.calvin.face.RetinaFaceDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸检测大模型.
 * @author Calvin
 * @date 2021-06-09
 * @email 179209347@qq.com
 */
public final class RetinaFaceDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(RetinaFaceDetectionExample.class);

  private RetinaFaceDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path facePath = Paths.get("src/test/resources/largest_selfie.jpg");
    Image img = ImageFactory.getInstance().fromFile(facePath);
    int topK = 500;
    double confThresh = 0.85f;
    double nmsThresh = 0.45f;
    DetectedObjects detection = RetinaFaceDetection.predict(img, topK, confThresh, nmsThresh);
    saveBoundingBoxImage(img, detection);
    logger.info("{}", detection);
  }

  private static void saveBoundingBoxImage(Image img, DetectedObjects detection)
      throws IOException {
    Path outputDir = Paths.get("build/output");
    Files.createDirectories(outputDir);

    // Make image copy with alpha channel because original image was jpg
    Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
    newImage.drawBoundingBoxes(detection);

    Path imagePath = outputDir.resolve("retinaface_detected.png");
    newImage.save(Files.newOutputStream(imagePath), "png");
    logger.info("Face detection result image has been saved in: {}", imagePath);
  }
}
