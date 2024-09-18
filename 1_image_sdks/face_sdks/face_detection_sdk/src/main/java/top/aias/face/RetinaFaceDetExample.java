package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.face.detection.FaceDetection;
import top.aias.face.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸检测大模型（更高精度，但是速度更慢）
 * Face detection model.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class RetinaFaceDetExample {

  private static final Logger logger = LoggerFactory.getLogger(RetinaFaceDetExample.class);

  private RetinaFaceDetExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path facePath = Paths.get("src/test/resources/beauty.jpg");
    Image img = OpenCVImageFactory.getInstance().fromFile(facePath);

    try (FaceDetection predictor = new FaceDetection();) {
      long start = System.currentTimeMillis();
      DetectedObjects detections = predictor.predict(img);
      long end = System.currentTimeMillis();
      System.out.println("time: "+ (end - start));

      ImageUtils.saveBoundingBoxImage(img, detections, "retinaface_detected.png", "build/output");
      logger.info("{}", detections);
    }
  }
}
