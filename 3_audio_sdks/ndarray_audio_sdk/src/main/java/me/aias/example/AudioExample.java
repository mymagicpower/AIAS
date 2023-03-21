package me.aias.example;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import me.aias.example.util.AudioArrayUtils;
import me.aias.example.util.AudioUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class AudioExample {
    private static final Logger logger = LoggerFactory.getLogger(AudioExample.class);

    // 测试 - test
    public static void main(String[] args) throws Exception {
        NDManager manager = NDManager.newBaseManager(Device.cpu());
        float[] floatArray = AudioArrayUtils.floatData("src/test/resources/test.wav");
        // 音频的float数组
        // Audio float array
        logger.info("Audio float array: {}", Arrays.toString(floatArray));

        NDArray samples = manager.create(floatArray);
        float rmsDb = AudioUtils.rmsDb(samples);
        // 返回以分贝为单位的音频均方根能量
        //  Audio root-mean-square energy in decibels
        logger.info("root-mean-square energy in decibels: {}", rmsDb);

        //提取特征前将音频归一化至-20 dB(以分贝为单位)
        //Normalize audio to -20 dB (in decibels) before feature extraction
        float target_dB = -20f;
        samples = AudioUtils.normalize(samples, target_dB);
        System.out.println("Normalize audio: " + samples.toDebugString(1000000000, 1000, 1000, 1000));

        // 生成帧的跨步大小(以毫秒为单位)
        // Frame step size in milliseconds for generating frames
        float stride_ms = 10f;
        // 用于生成帧的窗口大小(毫秒)
        // Window size in milliseconds used for generating frames
        float window_ms = 20f;
        samples = AudioUtils.linearSpecgram(manager, samples, stride_ms, window_ms);
        logger.info("Calculate linear spectrogram: {}", samples.toDebugString(1000000000, 1000, 10, 1000));
    }
}