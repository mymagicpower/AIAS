package me.aias.example;

import me.aias.example.utils.Utils;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;
import java.io.IOException;
import java.util.EnumSet;

/**
 * 显示中文及当前时间（使用字库支持中文字符）
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class LiveStreamFilterExample {

    public static void main(String[] args) throws IOException {
        //下载字体
        Utils.downloadFont();
        
        String rtspSrc = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
        String des = "build/output/live.mp4";
        //显示中文字符及时间
        String fontPath = "src/test/resources/simfang.ttf";//中文字体文件路径
        String filterContent = "drawtext=fontsize=14:fontcolor=red:fontfile=" + fontPath + ":text='实时时间\\: %{localtime\\:%Y\\-%m\\-%d %H：%M：%S}':x=10:y=10";

        liveRecord(rtspSrc, des, filterContent, 1, 0.1f);
    }

    /**
     * 录制视频
     *
     * @param src - 网络直播/录播地址
     * @param des - 文件地址
     */
    public static void liveRecord(String src, String des, String filterContent, int audioChannel, float mins)
            throws FrameGrabber.Exception, FrameFilter.Exception {
        // 获取视频源
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(src)) {
            // 开始取视频源
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

            //设置过滤器内容，具体参考http://ffmpeg.org/ffmpeg-filters.html
            FFmpegFrameFilter filter = new FFmpegFrameFilter(filterContent, width, height);
            filter.start();

            CanvasFrame canvas = new CanvasFrame("画面预览");// 新建一个预览窗口
            canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvas.setAlwaysOnTop(true);

            CanvasFrame canvasFrame =
                    new CanvasFrame("Camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvasFrame.setAlwaysOnTop(true);

            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            try (FFmpegFrameRecorder recorder =
                         new FFmpegFrameRecorder(des, width, height, audioChannel)) {
                // 开始录视频
                recorder.start();
                Frame frame = null;

                // 获取当前系统时间
                long startTime = System.currentTimeMillis();

                while ((frame = grabber.grabImage()) != null) {
                    //把抓取到的摄像头画面塞进过滤器
                    filter.push(frame);
                    //取出过滤器合并后的图像
                    Frame filterFrame = filter.pullImage();
                    // 显示过滤器处理后的画面
                    canvas.showImage(filterFrame);

                    recorder.record(filterFrame);
                    //获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数
                    long endTime = System.currentTimeMillis();
                    long usedTime = (endTime - startTime) / 1000;
                    if (usedTime > mins * 60) break;
                }
                // 关闭窗口
                canvasFrame.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
