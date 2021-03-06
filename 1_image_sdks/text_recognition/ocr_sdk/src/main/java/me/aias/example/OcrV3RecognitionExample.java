package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.ImageUtils;
import me.aias.example.utils.OcrV3Recognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR V3模型 文字识别.
 *
 * @author Calvin
 * @date 2021-10-07
 * @email 179209347@qq.com
 */
public final class OcrV3RecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(OcrV3RecognitionExample.class);

  private OcrV3RecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_0.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    OcrV3Recognition recognition = new OcrV3Recognition();
    try (ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
        ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
        Predictor<Image, String> recognizer = recognitionModel.newPredictor()) {

      long timeInferStart = System.currentTimeMillis();
      DetectedObjects detections = recognition.predict(image, detector, recognizer);
      long timeInferEnd = System.currentTimeMillis();
      System.out.println("time: " + (timeInferEnd - timeInferStart));

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(image, detections, "ocr_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
