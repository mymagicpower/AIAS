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
import me.aias.example.utils.RetinaFaceDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸检测大模型.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class RetinaFaceDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(RetinaFaceDetectionExample.class);

  private RetinaFaceDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path facePath = Paths.get("src/test/resources/largest_selfie.jpg");
    Image img = ImageFactory.getInstance().fromFile(facePath);
    // topk值
    int topK = 500;
    // 置信度阈值
    double confThresh = 0.85f;
    // 非极大值抑制阈值
    double nmsThresh = 0.45f;
    RetinaFaceDetection retinaFaceDetection = new RetinaFaceDetection();
    try (ZooModel<Image, DetectedObjects> model =
            ModelZoo.loadModel(retinaFaceDetection.criteria(topK, confThresh, nmsThresh));
        Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(img);
      ImageUtils.saveBoundingBoxImage(img, detections, "retinaface_detected.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
