package me.aias.example;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder.Exception;

import java.util.EnumSet;

/**
 * rtsp/rtmp 拉流录制视频
 *
 * @author Calvin
 */
public class LiveStream2MP4 {
    public static void main(String[] args) throws FrameGrabber.Exception {
        // rtmp://58.200.131.2:1935/livetv/cctv1
        // rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
        String src = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

        String des = "build/output/live.mp4";
        // audioChannel用于控制是否录制音频（0:不录制/1:录制)
        // 录制1分钟视频
        liveRecord(src, des, 1, 1);
    }

    /**
     * 录制视频
     *
     * @param src - 网络直播/录播地址
     * @param des - 文件地址
     */
    public static void liveRecord(String src, String des, int audioChannel, int mins)
            throws FrameGrabber.Exception {
        // 获取视频源
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(src)) {
            // 开始取视频源
            grabber.start();
            // 抓取一帧视频,用于获取高度/宽度
            Frame grabFrame = null;
            while ((grabFrame = grabber.grab()) != null) {
                EnumSet<Frame.Type> videoOrAudio = grabFrame.getTypes();
                if (videoOrAudio.contains(Frame.Type.VIDEO)) {
                    break;
                }
            }

            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            try (FFmpegFrameRecorder recorder =
                         new FFmpegFrameRecorder(des, grabFrame.imageWidth, grabFrame.imageHeight, audioChannel)) {
                // 开始录视频
                recorder.start();
                Frame frame = null;

                // 获取当前系统时间
                long startTime = System.currentTimeMillis();

                while ((frame = grabber.grabFrame()) != null) {
                    recorder.record(frame);
                    //获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数
                    long endTime = System.currentTimeMillis();
                    long usedTime = (endTime - startTime) / 1000;
                    if (usedTime > mins * 60) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
