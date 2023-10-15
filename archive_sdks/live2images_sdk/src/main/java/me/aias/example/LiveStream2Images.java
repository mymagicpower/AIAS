package me.aias.example;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder.Exception;

import javax.swing.*;
import java.util.EnumSet;

/**
 * 连续截图
 *
 * @author Calvin
 */
public class LiveStream2Images {
  public static void main(String[] args)
      throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {

    // mode: 1-覆盖模式，0-连续模式
    String input = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//    input = "rtmp://58.200.131.2:1935/livetv/cctv1";
    
    record(input, "build/images/image-%03d.jpg", "0");
  }

  /**
   * 连续截图
   *
   * @param input 支持视频文件（mp4,flv,avi等）,流媒体地址（rtmp，rtsp,http-flv等）
   * @param output 图片
   * @param mode 模式（1-覆盖模式，0-连续截图，根据文件名称模板顺序生成）
   */
  public static void record(String input, String output, String mode)
      throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
    try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
      grabber.start();

      // 抓取一帧视频,用于获取高度/宽度
      Frame grabFrame = null;
      while ((grabFrame = grabber.grab()) != null) {
        EnumSet<Frame.Type> videoOrAudio = grabFrame.getTypes();
        if (videoOrAudio.contains(Frame.Type.VIDEO)) {
          break;
        }
      }

      try (FFmpegFrameRecorder recorder =
          new FFmpegFrameRecorder(output, grabFrame.imageWidth, grabFrame.imageHeight, 0)) {

        recorder.setFormat("image2");
        if (mode == null) {
          mode = "0";
        }
        recorder.setOption("update", mode);
        recorder.start();

        CanvasFrame canvas = new CanvasFrame("图像预览");
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame frame = null;

        // 只抓取图像画面
        int count =0;
        for (; (frame = grabber.grabImage()) != null; ) {
          try {
            // 显示画面
            canvas.showImage(frame);
            // 录制
            count++;
            if(count>350)
              recorder.record(frame);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        canvas.dispose();
      }
    }
  }
}
