package top.aias.face;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.face.detection.UltraLightFaceDetection;
import top.aias.face.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 轻量级人脸检测小模型.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class UltraFaceDetExample {

  private static final Logger logger = LoggerFactory.getLogger(UltraFaceDetExample.class);

  private UltraFaceDetExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path facePath = Paths.get("src/test/resources/ljm01.jpg");
    Image img = OpenCVImageFactory.getInstance().fromFile(facePath);

    try (UltraLightFaceDetection predictor = new UltraLightFaceDetection();) {
      
      long start = System.currentTimeMillis();
      DetectedObjects detections = predictor.predict(img);
      long end = System.currentTimeMillis();
      System.out.println("time: "+ (end - start));

      ImageUtils.saveBoundingBoxImage(img, detections, "light_detected.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
