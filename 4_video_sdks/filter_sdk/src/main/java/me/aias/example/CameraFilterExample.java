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
public class CameraFilterExample {

    public static void main(String[] args) throws IOException {
        //下载字体
        Utils.downloadFont();
        
        //静态中文字符叠加
        String fontPath = "src/test/resources/simfang.ttf";//中文字体文件路径
        String filterContent = "drawtext=fontsize=30:fontcolor=red:fontfile=" + fontPath + ":text='实时时间\\: %{localtime\\:%Y\\-%m\\-%d %H：%M：%S}':x=30:y=30";

        filter(filterContent);
    }

    /**
     * 使用字库支持简体中文字符水印
     *
     * @param filterContent -录制的文件路径
     * @throws Exception
     * @throws Exception
     */
    public static void filter(String filterContent)
            throws FrameGrabber.Exception, FrameFilter.Exception {
        // 本机摄像头默认0
        try (FrameGrabber cameraGrabber = FrameGrabber.createDefault(0)) {
            // 开启抓取器
            cameraGrabber.start();

            // 转换器
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            // 抓取一帧视频,用于获取高度/宽度
            Frame grabFrame = null;
            while ((grabFrame = cameraGrabber.grab()) != null) {
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
                    new CanvasFrame("Camera", CanvasFrame.getDefaultGamma() / cameraGrabber.getGamma());
            canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvasFrame.setAlwaysOnTop(true);

            Frame cameraFrame = null;
            // 抓取屏幕画面
            for (; canvas.isShowing() && (cameraFrame = cameraGrabber.grab()) != null; ) {
                //把抓取到的摄像头画面塞进过滤器
                filter.push(cameraFrame);

                //取出过滤器合并后的图像
                Frame filterFrame = filter.pullImage();

                // 显示过滤器处理后的画面
                canvas.showImage(filterFrame);
            }

            // 关闭窗口
            canvasFrame.dispose();
        }
    }
}
