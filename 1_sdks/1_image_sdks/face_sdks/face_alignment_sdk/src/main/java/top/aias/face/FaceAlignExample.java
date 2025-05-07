package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.face.detection.FaceDetection;
import top.aias.face.utils.FaceUtils;
import top.aias.face.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 人脸对齐
 * Face Align
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class FaceAlignExample {
  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    process("src/test/resources/beauty.jpg");
  }

  public static void process(String facePath)
          throws IOException, ModelException, TranslateException {
    Path path = Paths.get(facePath);
    Image image = new OpenCVImageFactory().fromFile(path);

    try (FaceDetection faceDetection = new FaceDetection(Device.cpu());) {
      DetectedObjects detections = faceDetection.predict(image);

      List<DetectedObjects.DetectedObject> list = detections.items();

      int index = 0;
      for (DetectedObjects.DetectedObject result : list) {
        BoundingBox box = result.getBoundingBox();

        Image img = FaceUtils.align(image, box);

        ImageUtils.saveImage(img, "face_align_" + index++ + ".png", "build/output");
      }

      // 保存检测人脸
      // Save the detected faces
      ImageUtils.saveBoundingBoxImage(image, detections, "faces_detected.png", "build/output");

    }
  }
}
