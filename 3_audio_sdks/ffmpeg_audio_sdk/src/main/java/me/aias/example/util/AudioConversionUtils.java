package me.aias.example.util;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;

/**
 * 音频参数转换（包含采样率、编码，位数，通道数）
 * 获取音频数组
 *
 * @author Calvin
 */
public class AudioConversionUtils {
    /**
     * 通用音频格式参数转换
     *
     * @param inputFile  -输入音频文件
     * @param outputFile -输出音频文件
     * @param audioCodec -音频编码
     * @param sampleRate -音频采样率
     */
    public static void convert(String inputFile, String outputFile, int audioCodec, int sampleRate,
                               int audioChannels) throws FrameGrabber.Exception, Exception {
        Frame audioSamples = null;
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile)) {
            // 开启抓取器
            grabber.start();
            System.out.println("AudioBitrate: " + grabber.getAudioBitrate());
            System.out.println("AudioCodec: " + grabber.getAudioCodec());
            System.out.println("Format: " + grabber.getFormat());
            System.out.println("SampleRate: " + grabber.getSampleRate());

            // 音频录制（输出地址，音频通道）
            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, audioChannels)) {
                recorder.setAudioOption("crf", "0");
                recorder.setAudioCodec(audioCodec);
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                recorder.setAudioChannels(audioChannels);
                recorder.setSampleRate(sampleRate);
                recorder.setAudioQuality(0);
                recorder.setAudioOption("aq", "10");
                // 开启录制器
                recorder.start();
                try {
                    // 抓取音频
                    while ((audioSamples = grabber.grab()) != null) {
                        recorder.setTimestamp(grabber.getTimestamp());
                        recorder.record(audioSamples);
                    }

                } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                    System.err.println("抓取失败");
                } catch (Exception e) {
                    System.err.println("录制失败");
                }
            }

        }
    }

}