package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FaceDetection;
import me.aias.example.utils.FaceMaskDetect;
import me.aias.example.utils.OpenCVImageUtil;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.*;

/**
 * 本地视频口罩检测
 * Local video face mask detection
 *
 * @author Calvin
 */
public class MP4FaceMaskDetectionExample {
  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    faceMaskDetection("src/test/resources/mask.mp4");
  }

  /**
   * 人脸检测
   * Face detection
   *
   * @param input 视频源 - video source
   */
  public static void faceMaskDetection(String input)
      throws IOException, ModelException, TranslateException {
    float shrink = 0.5f;
    float threshold = 0.85f;
    Criteria<Image, DetectedObjects> criteria = new FaceDetection().criteria(shrink, threshold);
    Criteria<Image, Classifications> maskCriteria = new FaceMaskDetect().criteria();

    // Read video file or video stream to get image (the obtained image is of type frame, which needs to be converted to mat type for detection and recognition)
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
    grabber.start();

    // Frame与Mat转换
    // Frame to Mat conversion
    OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    CanvasFrame canvas = new CanvasFrame("Face Detections");
    canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setVisible(true);
    canvas.setFocusable(true);
    // 窗口置顶 - Top window
    if (canvas.isAlwaysOnTopSupported()) {
      canvas.setAlwaysOnTop(true);
    }
    Frame frame = null;

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, DetectedObjects> predictor = model.newPredictor();
        ZooModel classifyModel = ModelZoo.loadModel(maskCriteria);
        Predictor<Image, Classifications> classifier = classifyModel.newPredictor()) {
      // 获取图像帧
      // Get image frame
      for (; canvas.isVisible() && (frame = grabber.grabImage()) != null; ) {

        // 将获取的frame转化成mat数据类型
        // Convert the obtained frame to mat data type
        Mat img = converter.convert(frame);
        BufferedImage buffImg = OpenCVImageUtil.mat2BufferedImage(img);

        Image image = ImageFactory.getInstance().fromImage(buffImg);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        DetectedObjects detections = predictor.predict(image);
        List<DetectedObjects.DetectedObject> items = detections.items();

        // 遍历人脸
        // Traverse faces
        for (DetectedObjects.DetectedObject item : items) {
          Image subImg = getSubImage(image, item.getBoundingBox());
          Classifications classifications = classifier.predict(subImg);
          String className = classifications.best().getClassName();

          BoundingBox box = item.getBoundingBox();
          Rectangle rectangle = box.getBounds();
          int x = (int) (rectangle.getX() * imageWidth);
          int y = (int) (rectangle.getY() * imageHeight);
          Rect face =
              new Rect(
                  x,
                  y,
                  (int) (rectangle.getWidth() * imageWidth),
                  (int) (rectangle.getHeight() * imageHeight));

          // 绘制人脸矩形区域，scalar色彩顺序：BGR(蓝绿红)
          // Draw face rectangle, scalar color order: BGR (blue, green, red)
          rectangle(img, face, new Scalar(0, 0, 255, 1));

          int pos_x = Math.max(face.tl().x() - 10, 0);
          int pos_y = Math.max(face.tl().y() - 10, 0);
          // 在人脸矩形上面绘制文字
          // Draw text above the face rectangle
          putText(
              img,
              className,
              new Point(pos_x, pos_y),
              FONT_HERSHEY_COMPLEX,
              1.0,
              new Scalar(0, 0, 255, 2.0));
        }

        // 显示视频图像
        // Display video image
        canvas.showImage(frame);
      }
    }

    canvas.dispose();
    grabber.close();
  }

  private static int[] extendSquare(
      double xmin, double ymin, double width, double height, double percentage) {
    double centerx = xmin + width / 2;
    double centery = ymin + height / 2;
    double maxDist = Math.max(width / 2, height / 2) * (1 + percentage);
    return new int[] {(int) (centerx - maxDist), (int) (centery - maxDist), (int) (2 * maxDist)};
    //    return new int[] {(int) xmin, (int) ymin, (int) width, (int) height};
  }

  private static Image getSubImage(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    int width = img.getWidth();
    int height = img.getHeight();
    int[] squareBox =
        extendSquare(
            rect.getX() * width,
            rect.getY() * height,
            rect.getWidth() * width,
            rect.getHeight() * height,
            0); // 0.18

    if (squareBox[0] < 0) squareBox[0] = 0;
    if (squareBox[1] < 0) squareBox[1] = 0;
    if (squareBox[0] > width) squareBox[0] = width;
    if (squareBox[1] > height) squareBox[1] = height;
    if ((squareBox[0] + squareBox[2]) > width) squareBox[2] = width - squareBox[0];
    if ((squareBox[1] + squareBox[2]) > height) squareBox[2] = height - squareBox[1];
    return img.getSubImage(squareBox[0], squareBox[1], squareBox[2], squareBox[2]);
    //    return img.getSubimage(squareBox[0], squareBox[1], squareBox[2], squareBox[3]);
  }
}
