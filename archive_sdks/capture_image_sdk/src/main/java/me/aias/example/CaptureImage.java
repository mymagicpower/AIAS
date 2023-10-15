package me.aias.example;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 截图
 *
 * @author Calvin
 */
public class CaptureImage {
  public static void main(String[] args) throws Exception, FrameGrabber.Exception {

    String input = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

    captureBufferdImage(input, "build/output/capture.png", "png");
  }

  /**
   * 截一张图
   *
   * @param input 支持视频文件（mp4,flv,avi等）,流媒体地址（rtmp，rtsp,http-flv等）
   * @param output 截图保存路径或者地址
   * @param format 截图格式，比如：jpg,png等
   */
  public static void captureBufferdImage(String input, String output, String format)
      throws FrameGrabber.Exception {
    try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
      grabber.start();

      Frame frame = null;
      Java2DFrameConverter converter = new Java2DFrameConverter();
      // 抓取图像画面
      if ((frame = grabber.grabImage()) != null) {
        BufferedImage image = converter.convert(frame);
        try {
          ImageIO.write(image, format, new FileOutputStream(output));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
