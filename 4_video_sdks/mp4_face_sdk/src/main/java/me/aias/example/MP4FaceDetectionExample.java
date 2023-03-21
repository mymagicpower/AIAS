package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
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
 * 本地视频人脸检测
 * Local video face detection
 * @author Calvin
 */
public class MP4FaceDetectionExample {
  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    faceDetection("src/test/resources/test.mp4");
  }

  /**
   * 人脸检测
   * Face detection
   *
   * @param input 视频源 - input video source
   */
  public static void faceDetection(String input)
      throws IOException, ModelException, TranslateException {
    float shrink = 0.5f;
    float threshold = 0.7f;
    Criteria<Image, DetectedObjects> criteria = new FaceDetection().criteria(shrink, threshold);

    // 读取视频文件或者视频流获取图像（得到的图像为frame类型，需要转换为mat类型进行检测和识别）
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
        Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
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
              "Face",
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
}
