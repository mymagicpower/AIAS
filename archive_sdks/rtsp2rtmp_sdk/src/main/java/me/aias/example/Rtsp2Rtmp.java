package me.aias.example;

import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber.Exception;

import java.io.IOException;

import static org.bytedeco.ffmpeg.global.avcodec.av_free_packet;

/**
 * rtsp转rtmp（转封装方式）
 *
 * @author Calvin
 */
public class Rtsp2Rtmp {
    // 采集/抓取器
    private FFmpegFrameGrabber grabber = null;
    private FFmpegFrameRecorder record = null;
    // 视频宽度，高度
    private int width = -1;
    private int height = -1;
    // 音/视频编码参数
    private int audioCodec;
    private int videoCodec;
    // 帧率
    private double frameRate;
    // 比特率
    private int bitRate;
    // 音频参数
    private int audioChannels;
    private int audioBitRate;
    private int sampleRate;

    public static void main(String[] args) throws Exception, IOException {

        // 运行，设置视频源和推流地址
        new Rtsp2Rtmp()
                .from("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov")
                .to("rtmp://push.xxx.xxx/live/video")
                .run();
    }

    /**
     * 选择视频源
     *
     * @param src
     * @throws Exception
     */
    public Rtsp2Rtmp from(String src) throws Exception {
        // 抓取器
        grabber = new FFmpegFrameGrabber(src);
        if (src.indexOf("rtsp") >= 0) {
            grabber.setFormat("rtsp");
            // 设置RTSP传输协议为tcp传输模式
            grabber.setOption("rtsp_transport", "tcp");
            // socket网络超时时间
            grabber.setOption("stimeout", "3000000");
        }
        // ffmpeg采集视频信息
        grabber.start();
        width = grabber.getImageWidth();
        height = grabber.getImageHeight();

        // 音频编码
        audioCodec = grabber.getAudioCodec();
        // 视频编码
        videoCodec = grabber.getVideoCodec();
        // 帧率
        frameRate = grabber.getVideoFrameRate();
        // 比特率
        bitRate = grabber.getVideoBitrate();
        // 音频参数
        audioChannels = grabber.getAudioChannels();
        audioBitRate = grabber.getAudioBitrate();
        sampleRate = grabber.getSampleRate();
        if (audioBitRate < 1) {
            audioBitRate = 128 * 1000; // 默认音频比特率
        }
        return this;
    }

    /**
     * 选择输出
     *
     * @param des
     * @throws IOException
     */
    public Rtsp2Rtmp to(String des) throws IOException {
        // 录制/推流器
        record = new FFmpegFrameRecorder(des, width, height);
        record.setVideoOption("crf", "18");
        record.setGopSize(2);
        record.setFrameRate(frameRate);
        record.setVideoBitrate(bitRate);
        record.setAudioChannels(audioChannels);
        record.setAudioBitrate(audioBitRate);
        record.setSampleRate(sampleRate);
        AVFormatContext avFormatContext = null;
        // 封装格式flv
        record.setFormat("flv");
        record.setAudioCodecName("aac");
        record.setVideoCodec(videoCodec);
        avFormatContext = grabber.getFormatContext();
        record.start(avFormatContext);
        return this;
    }

    /**
     * 转封装：音视频帧直接推送（无需解码）
     *
     * @param
     */
    public Rtsp2Rtmp run() {
        // 错误计数
        int errCount = 0;
        int frameCount = 0;

        // 连续3次没有采集到帧或者程序错误次数超过3次中断程序
        while (frameCount < 3 || errCount > 3) {
            AVPacket avPacket = null;
            try {
                // 没有解码的音视频帧
                avPacket = grabber.grabPacket();
                if (avPacket == null || avPacket.size() <= 0 || avPacket.data() == null) {
                    // 空包记录次数并跳过
                    frameCount++;
                    continue;
                }
                // 推送
                // 如果失败errCount自增1
                errCount += (record.recordPacket(avPacket) ? 0 : 1);
                av_free_packet(avPacket);
            } catch (Exception e) {
                errCount++;
            } catch (IOException e) {
                errCount++;
            }
        }
        return this;
    }
}
