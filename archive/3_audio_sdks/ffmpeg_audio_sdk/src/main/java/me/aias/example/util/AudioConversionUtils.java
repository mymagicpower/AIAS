package me.aias.example.util;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;

/**
 * 音频参数转换（包含采样率、编码，位数，通道数）
 * Audio parameter conversion (including sampling rate, encoding, bit depth, channel number)
 * 获取音频数组
 * Get audio array
 *
 * @author Calvin
 */
public class AudioConversionUtils {
    /**
     * 通用音频格式参数转换
     * Common audio format parameter conversion
     *
     * @param inputFile  -输入音频文件 - Input audio file
     * @param outputFile -输出音频文件 - Output audio file
     * @param audioCodec -音频编码 - Audio encoding
     * @param sampleRate -音频采样率 - Audio sampling rate
     */
    public static void convert(String inputFile, String outputFile, int audioCodec, int sampleRate,
                               int audioChannels) throws FrameGrabber.Exception, Exception {
        Frame audioSamples = null;
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile)) {
            // 开启抓取器
            // Start grabber
            grabber.start();
            System.out.println("AudioBitrate: " + grabber.getAudioBitrate());
            System.out.println("AudioCodec: " + grabber.getAudioCodec());
            System.out.println("Format: " + grabber.getFormat());
            System.out.println("SampleRate: " + grabber.getSampleRate());

            // 音频录制（输出地址，音频通道）
            // Audio recording (output address, audio channel)
            try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, audioChannels)) {
                recorder.setAudioOption("crf", "0");
                recorder.setAudioCodec(audioCodec);
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                recorder.setAudioChannels(audioChannels);
                recorder.setSampleRate(sampleRate);
                recorder.setAudioQuality(0);
                recorder.setAudioOption("aq", "10");
                // 开启录制器
                // Start recorder
                recorder.start();
                try {
                    // 抓取音频
                    // Grab audio
                    while ((audioSamples = grabber.grab()) != null) {
                        recorder.setTimestamp(grabber.getTimestamp());
                        recorder.record(audioSamples);
                    }

                } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                    System.err.println("抓取失败 - Failed to grab");
                } catch (Exception e) {
                    System.err.println("录制失败 - Recording failed");
                }
            }

        }
    }

}