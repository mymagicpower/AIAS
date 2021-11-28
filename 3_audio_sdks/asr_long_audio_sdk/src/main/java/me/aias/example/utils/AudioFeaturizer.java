package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * 音频特征器,用于从AudioSegment或SpeechSegment内容中提取特性。
 *
 * @author Calvin <179209347@qq.com>
 */
public class AudioFeaturizer {

  /**
   * 从AudioSegment或SpeechSegment中提取音频特征
   *
   * @param manager
   * @param floatArray
   * @return
   * @throws Exception
   */
  public static NDArray featurize(NDManager manager, float[] floatArray) {
    // 音频归一化
    NDArray samples = manager.create(floatArray);
    float rmsDb = AudioUtils.rmsDb(samples);
    // 返回以分贝为单位的音频均方根能量
    System.out.println("音频均方根能量: " + rmsDb);

    // 提取特征前将音频归一化至-20 dB(以分贝为单位)
    float target_dB = -20f;
    samples = AudioUtils.normalize(samples, target_dB);

    // 生成帧的跨步大小(以毫秒为单位)
    float stride_ms = 10f;
    // 用于生成帧的窗口大小(毫秒)
    float window_ms = 20f;
    // 用快速傅里叶变换计算线性谱图
    NDArray specgram = AudioUtils.linearSpecgram(manager, samples, stride_ms, window_ms);
    // System.out.println(specgram.toDebugString(1000000000, 1000, 10, 1000));

    return specgram;
  }
}
