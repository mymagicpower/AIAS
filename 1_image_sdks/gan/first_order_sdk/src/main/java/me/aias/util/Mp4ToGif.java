package me.aias.util;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.librealsense.frame;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * mp4视频文件转gif动态图
 * mp4 video file to gif animation
 *
 * @author Calvin
 */
public class Mp4ToGif {

    public static void main(String[] args) throws Exception, UnsupportedEncodingException, FileNotFoundException, org.bytedeco.javacv.FrameRecorder.Exception {
        String input = "build/output/result.mp4";
        String output = "build/output/result.gif";
        transToGif(input, 256, 256, 25, output);
    }

    /**
     * mp4转gif动态图
     * mp4 to gif animation
     * @param input
     * @param width
     * @param height
     * @param frameRate
     * @param output
     */
    public static void transToGif(String input, Integer width, Integer height, Integer frameRate, String output) throws FileNotFoundException, Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
            grabber.start();

            if (width == null || height == null) {
                width = grabber.getImageWidth();
                height = grabber.getImageHeight();
            }

            // gif录制器
            // gif recorder
            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, 0)) {
                // 设置像素格式
                // set pixel format
                recorder.setPixelFormat(avutil.AV_PIX_FMT_RGB4_BYTE);
                //设置编码 - set encoding
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_GIF);
                //设置帧率 - set frame rate
                if (frameRate != null) {
                    recorder.setFrameRate(frameRate);
                }
                recorder.start();

                CanvasFrame canvas = new CanvasFrame("转换gif中屏幕预览 - Converting to gif and previewing on screen");
                canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                canvas.setAlwaysOnTop(true);
                Frame frame = null;

                // 只抓取图像画面 - only grab image frames
                for (; (frame = grabber.grabImage()) != null; ) {
                    try {
                        //录制 - record
                        recorder.record(frame);
                        //显示画面 - show image
                        canvas.showImage(frame);
                    } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                        e.printStackTrace();
                    }
                }
//                //close包含stop和release方法 - close includes stop and release methods
//                recorder.close();
//                grabber.close();
                canvas.dispose();
            }
        }
    }
}