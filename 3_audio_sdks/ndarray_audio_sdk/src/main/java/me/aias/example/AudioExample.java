package me.aias.example;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import me.aias.example.util.AudioArrayUtils;
import me.aias.example.util.AudioUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class AudioExample {
    private static final Logger logger = LoggerFactory.getLogger(AudioExample.class);

    // 测试
    public static void main(String[] args) throws Exception {
        NDManager manager = NDManager.newBaseManager(Device.cpu());
        float[] floatArray = AudioArrayUtils.floatData("src/test/resources/test.wav");
        //音频的float数组
        logger.info("音频的float数组: {}", Arrays.toString(floatArray));

        NDArray samples = manager.create(floatArray);
        float rmsDb = AudioUtils.rmsDb(samples);
        //返回以分贝为单位的音频均方根能量
        logger.info("音频均方根能量: {}", rmsDb);

        //提取特征前将音频归一化至-20 dB(以分贝为单位)
        float target_dB = -20f;
        samples = AudioUtils.normalize(samples, target_dB);
        System.out.println("音频归一化: " + samples.toDebugString(1000000000, 1000, 1000, 1000));

        //生成帧的跨步大小(以毫秒为单位)
        float stride_ms = 10f;
        //用于生成帧的窗口大小(毫秒)
        float window_ms = 20f;
        samples = AudioUtils.linearSpecgram(manager, samples, stride_ms, window_ms);
        logger.info("快速傅里叶变换计算线性谱图: {}", samples.toDebugString(1000000000, 1000, 10, 1000));
    }
}