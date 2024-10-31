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
 * Rtsp取流口罩检测
 *
 * @author Calvin
 */
public class RtspFaceMaskDetectionExample {
  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    // 海康/大华等摄像机的rtsp地址：rtsp://user:password@192.168.16.100:554/Streaing/Channels/1
    // 海康/大华等视频平台的rtsp地址：rtsp://192.168.16.88:554/openUrl/6rcShva
    // 换成自己的rtsp地址
    String rtsp ="";
    faceMaskDetection(rtsp);
  }

  /**
   * 口罩检测
   *
   * @param input 视频源
   */
  public static void faceMaskDetection(String input)
      throws IOException, ModelException, TranslateException {
    float shrink = 0.5f;
    float threshold = 0.7f;
    Criteria<Image, DetectedObjects> criteria = new FaceDetection().criteria(shrink, threshold);
    Criteria<Image, Classifications> maskCriteria = new FaceMaskDetect().criteria();

    // 读取视频文件或者视频流获取图像（得到的图像为frame类型，需要转换为mat类型进行检测和识别）
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
    if (input.indexOf("rtsp") > -1) {
      grabber.setFormat("rtsp");
      // 设置要从服务器接受的媒体类型，为空默认支持所有媒体类型，支持的媒体类型：[video，audio，data]
      grabber.setOption("allowed_media_types", "video");
      // 设置RTSP传输协议为tcp传输模式
      grabber.setOption("rtsp_transport", "tcp");
      /*
       * rtsp_flags:[filter_src,prefer_tcp,listen]
       * filter_src:仅接受来自协商对等地址和端口的数据包。
       * prefer_tcp:如果TCP可用作RTSP RTP传输，请首先尝试使用TCP进行RTP传输。
       * listen:充当rtsp服务器，监听rtsp连接
       * rtp传输首选使用tcp传输模式
       */
      grabber.setOption("rtsp_flags", "prefer_tcp");
      /*
       * 设置等待传入连接最大超时时间（单位：秒），默认值-1无限等待
       * 如果设置此选项，上面的rtsp_flags配置将被设置成“listen”，充当rtsp服务器，监听rtsp连接
       */
      // grabber.setOption("timeout","30");

      // socket网络超时时间
      grabber.setOption("stimeout", "3000000");

      // 设置要缓冲以处理重新排序的数据包的数据包数量
      //			grabber.setOption("reorder_queue_size","");

      // 设置本地最小的UDP端口，默认为5000端口。
      //			grabber.setOption("min_port","5000");
      // 设置本地最大的UDP端口，默认为65000端口。
      //			grabber.setOption("max_port","65000");
    }
    grabber.start();

    // Frame与Mat转换
    OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    CanvasFrame canvas = new CanvasFrame("人脸检测"); // 新建一个预览窗口
    canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setVisible(true);
    canvas.setFocusable(true);
    // 窗口置顶
    if (canvas.isAlwaysOnTopSupported()) {
      canvas.setAlwaysOnTop(true);
    }
    Frame frame = null;

    try (ZooModel model = ModelZoo.loadModel(criteria);
         Predictor<Image, DetectedObjects> predictor = model.newPredictor();
         ZooModel classifyModel = ModelZoo.loadModel(maskCriteria);
         Predictor<Image, Classifications> classifier = classifyModel.newPredictor()) {
      // 获取图像帧
      for (; canvas.isVisible() && (frame = grabber.grabImage()) != null; ) {

        // 将获取的frame转化成mat数据类型
        Mat img = converter.convert(frame);
        BufferedImage buffImg = OpenCVImageUtil.mat2BufferedImage(img);

        Image image = ImageFactory.getInstance().fromImage(buffImg);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        DetectedObjects detections = predictor.predict(image);
        List<DetectedObjects.DetectedObject> items = detections.items();

        // 遍历人脸
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
          rectangle(img, face, new Scalar(0, 0, 255, 1));

          int pos_x = Math.max(face.tl().x() - 10, 0);
          int pos_y = Math.max(face.tl().y() - 10, 0);
          // 在人脸矩形上面绘制文字
          putText(
              img,
              className,
              new Point(pos_x, pos_y),
              FONT_HERSHEY_COMPLEX,
              1.0,
              new Scalar(0, 0, 255, 2.0));
        }

        // 显示视频图像
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
