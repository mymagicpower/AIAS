package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.calvin.ocr.OcrDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR文字检测.
 * @author Calvin
 * @date 2021-06-28
 * @email 179209347@qq.com
 */
public final class OcrDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(OcrDetectionExample.class);

  private OcrDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_0.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    DetectedObjects detection = OcrDetection.predict(image);

    List<DetectedObjects.DetectedObject> boxes = detection.items();
    for (DetectedObjects.DetectedObject result : boxes) {
      // result.getClassName() 为图片的旋转角度
      // result.getProbability() 为置信度
      System.out.println(result.getClassName() + " : " + result.getProbability());
    }
    
    saveBoundingBoxImage(image, detection);
    logger.info("{}", detection);
  }

  public static void saveBoundingBoxImage(Image img, DetectedObjects detection) throws IOException {
    // Make image copy with alpha channel because original image was jpg
    Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
    newImage.drawBoundingBoxes(detection);
    Path outputDir = Paths.get("build/output");
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve("detect_result.png");
    // OpenJDK can't save jpg with alpha channel
    newImage.save(Files.newOutputStream(imagePath), "png");
    logger.info("Result image has been saved in: {}", imagePath);
  }
}
