package me.aias.example;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

import javax.swing.*;
import java.io.FileNotFoundException;

/**
 * 批量静态图转apng动态图
 *
 * @author Calvin
 */
public class Images2Apng {
    public static void main(String[] args) throws Exception, FileNotFoundException, FrameRecorder.Exception {
        String input = "build/images/image-%03d.jpg";
        String output = "build/output/images2Apng.apng";

        transToPng(input, output, 240, 160, 25);
    }

    /**
     * 批量图片转apng动态图
     *
     * @param input     批量图片
     * @param output    录制的apng地址
     * @param width     宽度
     * @param height    高度
     * @param frameRate 帧率
     */
    public static void transToPng(String input, String output, Integer width, Integer height, Integer frameRate) throws FileNotFoundException, Exception, FrameRecorder.Exception {

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input)) {
            grabber.setFormat("image2");
            //如果设置为1，则循环输入。默认为0
            grabber.setOption("loop", "0");
            //序列模式
            grabber.setOption("pattern_type", "sequence");
            grabber.start();
            if (width == null || height == null) {
                width = grabber.getImageWidth();
                height = grabber.getImageHeight();
            }

            //apng录制器
            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, 0)) {
                //设置像素格式
                recorder.setPixelFormat(avutil.AV_PIX_FMT_RGBA);
                //设置录制的视频/图片编码
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_APNG);
                if (frameRate != null) {
                    recorder.setFrameRate(frameRate);//设置帧率
                }
                recorder.start();

                CanvasFrame canvas = new CanvasFrame("转换apng中屏幕预览");
                canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                canvas.setAlwaysOnTop(true);
                Frame frame = null;

                // 只抓取图像画面
                for (; canvas.isShowing() && (frame = grabber.grabImage()) != null; ) {
                    try {
                        //录制
                        recorder.record(frame);
                        //显示画面
                        canvas.showImage(frame);
                    } catch (FrameRecorder.Exception e) {
                        e.printStackTrace();
                    }
                }
                canvas.dispose();
            }

        }
    }
}