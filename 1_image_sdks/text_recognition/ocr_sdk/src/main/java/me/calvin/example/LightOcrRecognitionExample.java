package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import me.calvin.ocr.OcrRecognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * OCR文字识别.
 * @author Calvin
 * @date 2021-06-10
 * @email 179209347@qq.com
 */
public final class LightOcrRecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(LightOcrRecognitionExample.class);

  private LightOcrRecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/freetxt.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    DetectedObjects detection = OcrRecognition.predict(image);
    saveBoundingBoxImage(image, detection);
    logger.info("{}", detection);
  }

  public static void saveBoundingBoxImage(Image img, DetectedObjects detection) throws IOException {
    // Make image copy with alpha channel because original image was jpg
    Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
    newImage.drawBoundingBoxes(detection);
    Path outputDir = Paths.get("build/output");
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve("ocr_result.png");
    // OpenJDK can't save jpg with alpha channel
    newImage.save(Files.newOutputStream(imagePath), "png");
    logger.info("Result image has been saved in: {}", imagePath);
  }
}
