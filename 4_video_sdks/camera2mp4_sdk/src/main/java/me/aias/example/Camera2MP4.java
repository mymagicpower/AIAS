package me.aias.example;

import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;
import java.util.EnumSet;

/**
 * 按帧录制本机摄像头视频（边预览边录制）
 *
 * @author Calvin
 */
public class Camera2MP4 {

    public static void main(String[] args)
            throws Exception,
            FrameGrabber.Exception {
        // audioChannel用于控制是否录制音频（0:不录制/1:录制)
        // 录制1分钟视频
        recordCamera("build/output/camera.mp4", 1, 1);
    }

    /**
     * 按帧录制本机摄像头视频（边预览边录制）
     *
     * @param outputFile -录制的文件路径
     * @throws Exception
     * @throws Exception
     */
    public static void recordCamera(String outputFile, int audioChannel, float mins)
            throws FrameGrabber.Exception, Exception {
        // 本机摄像头默认0
        try (FrameGrabber grabber = FrameGrabber.createDefault(0)) {
            // 开启抓取器
            grabber.start();

            // 转换器
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            // 抓取一帧视频,用于获取高度/宽度
            Frame grabFrame = null;
            while ((grabFrame = grabber.grab()) != null) {
                EnumSet<Frame.Type> videoOrAudio = grabFrame.getTypes();
                if (videoOrAudio.contains(Frame.Type.VIDEO)) {
                    break;
                }
            }

            IplImage grabbedImage = converter.convert(grabFrame);
            int width = grabbedImage.width();
            int height = grabbedImage.height();

            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            try (FFmpegFrameRecorder recorder =
                         new FFmpegFrameRecorder(outputFile, width, height, audioChannel)) {
                recorder.start(); // 开启录制器
                CanvasFrame canvasFrame =
                        new CanvasFrame("Camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
                canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                canvasFrame.setAlwaysOnTop(true);

                //获取当前系统时间
                long startTime = System.currentTimeMillis();

                while (canvasFrame.isVisible() && (grabFrame = grabber.grab()) != null) {
                    EnumSet<Frame.Type> videoOrAudio = grabFrame.getTypes();
                    if (videoOrAudio.contains(Frame.Type.VIDEO)) {
                        canvasFrame.showImage(grabFrame);
                    }
                    recorder.record(grabFrame);

                    //获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数
                    long endTime = System.currentTimeMillis();
                    long usedTime = (endTime - startTime) / 1000;
                    if (usedTime > mins * 60) break;
                }
                // 关闭窗口
                canvasFrame.dispose();
            }
        }
    }
}
