package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import top.aias.ocr.utils.cls.OcrDirectionDetection;
import top.aias.ocr.utils.common.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.ocr.utils.common.RotatedBox;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR文字方向检测(轻量级模型).
 *
 * OCR text direction detection (light model)
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class DirectionDetExample {

  private static final Logger logger = LoggerFactory.getLogger(DirectionDetExample.class);

  private DirectionDetExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/ticket_90.png");
    Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);


    try (OcrDirectionDetection detection = new OcrDirectionDetection(Device.cpu());) {

      long start = System.currentTimeMillis();
      List<RotatedBox> detections = detection.predict(image);

      for (RotatedBox result : detections) {
        System.out.println(result.getText() + " : " + result.getProb());
      }

      long end = System.currentTimeMillis();
      System.out.println("Time: " + (end - start));

      for (RotatedBox result : detections) {
        ImageUtils.drawRectWithText((Mat) image.getWrappedImage(), result.getBox(), result.getText());
      }
      ImageUtils.saveImage(image, "cls_detect_result.png", "build/output");
    }
  }
}