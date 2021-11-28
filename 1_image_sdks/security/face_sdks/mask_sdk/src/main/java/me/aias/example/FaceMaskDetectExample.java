package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FaceDetection;
import me.aias.example.utils.FaceMaskDetect;
import me.aias.example.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 口罩检测模型.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */

public final class FaceMaskDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(FaceMaskDetect.class);

  private FaceMaskDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String facePath = "src/test/resources/masks.png";
    BufferedImage img = ImageIO.read(new File(facePath));
    Image image = ImageFactory.getInstance().fromImage(img);

    // 图像缩放比，越小速度越快，精度越低，需根据场景调优
    float scale = 0.5f;
    // 检测结果的置信度过滤阈值
    float threshold = 0.7f;
    FaceDetection faceDetection = new FaceDetection();
    FaceMaskDetect faceMaskDetect = new FaceMaskDetect();
    try (ZooModel<Image, DetectedObjects> model =
            ModelZoo.loadModel(faceDetection.criteria(scale, threshold));
         Predictor<Image, DetectedObjects> faceDetector = model.newPredictor();
         ZooModel classifyModel = ModelZoo.loadModel(faceMaskDetect.criteria());
         Predictor<Image, Classifications> classifier = classifyModel.newPredictor()) {

      DetectedObjects detections = faceMaskDetect.predict(faceDetector, classifier, image);
      //    List<DetectedObjects.DetectedObject> faces = detections.items();
      //    List<String> names = new ArrayList<>();
      //    List<Double> prob = new ArrayList<>();
      //    List<BoundingBox> rect = new ArrayList<>();
      //    for (DetectedObjects.DetectedObject face : faces) {
      //      prob.add(face.getProbability());
      //      rect.add(face.getBoundingBox());
      //    }

      logger.info(
          " Face mask detection result image has been saved in: {}",
          "build/output/faces_detected.png");
      ImageUtils.saveBoundingBoxImage(image, detections, "face_masks.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
