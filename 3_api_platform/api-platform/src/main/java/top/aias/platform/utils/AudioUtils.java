package top.aias.platform.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

public class AudioUtils {
    public static void main(String[] args) {
        String inputFilePath = "src/test/resources/4.mp4"; // 输入音频文件路径（可以是 flac, mp3, mp4 等）
        String outputFilePath = "src/test/resources/output.wav"; // 输出 WAV 文件路径
        convert(inputFilePath, outputFilePath);
    }

    public static void convert(String inputFilePath, String outputFilePath) {
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;

        try {
            // 初始化 grabber 用于读取输入音频
            grabber = new FFmpegFrameGrabber(inputFilePath);
            grabber.start();

            // 初始化 recorder 用于写入输出 WAV 文件
            recorder = new FFmpegFrameRecorder(outputFilePath, grabber.getAudioChannels());
            recorder.setFormat("wav");
            int a = grabber.getSampleRate();
            recorder.setSampleRate(16000);
            recorder.setAudioChannels(grabber.getAudioChannels());  // 设置音频通道数
            recorder.start();

            // 读取和写入音频帧
            Frame frame;
            while ((frame = grabber.grabSamples()) != null) {
                recorder.record(frame);
            }

            System.out.println("Conversion completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (grabber != null) grabber.stop();
                if (recorder != null) recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
