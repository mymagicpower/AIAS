package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FaceDetection;
import me.aias.example.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸检测模型.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */

public final class FaceDetectionExample {

  private static final Logger logger = LoggerFactory.getLogger(FaceDetectionExample.class);

  private FaceDetectionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/faces.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    //图像缩放比，越小速度越快，精度越低，需根据场景调优
    float scale = 0.5f;
    //检测结果的置信度过滤阈值
    float threshold = 0.7f;
    FaceDetection faceDetection = new FaceDetection();
    try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(faceDetection.criteria(scale,threshold));
         Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(image);
      logger.info(" Face detection result image has been saved in: {}", "build/output/faces_detected.png");
      ImageUtils.saveBoundingBoxImage(image, detections, "faces_detected.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
