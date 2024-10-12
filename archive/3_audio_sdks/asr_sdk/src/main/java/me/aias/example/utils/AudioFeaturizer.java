package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * 音频特征器,用于从AudioSegment或SpeechSegment内容中提取特性。
 * Audio feature extractor, used to extract features from AudioSegment or SpeechSegment content.
 *
 * @author Calvin <179209347@qq.com>
 */
public class AudioFeaturizer {

  /**
   * 从AudioSegment或SpeechSegment中提取音频特征
   * Extract audio features from AudioSegment or SpeechSegment
   *
   * @param manager
   * @param floatArray
   * @return
   * @throws Exception
   */
  public static NDArray featurize(NDManager manager, float[] floatArray) {
    // 音频归一化
    // Audio normalization
    NDArray samples = manager.create(floatArray);
    float rmsDb = AudioUtils.rmsDb(samples);
    // 返回以分贝为单位的音频均方根能量
    // Return the root mean square energy of the audio in decibels
    System.out.println("音频均方根能量 - he root mean square energy: " + rmsDb);

    // 提取特征前将音频归一化至-20 dB(以分贝为单位)
    // Normalize the audio to -20 dB before feature extraction (in decibels)
    float target_dB = -20f;
    samples = AudioUtils.normalize(samples, target_dB);

    // 生成帧的跨步大小(以毫秒为单位)
    // Generate the stride size of frames (in milliseconds)
    float stride_ms = 10f;
    // 用于生成帧的窗口大小(毫秒)
    // The window size (in milliseconds) used to generate frames
    float window_ms = 20f;
    // 用快速傅里叶变换计算线性谱图
    // Use fast Fourier transform to calculate a linear spectrogram
    NDArray specgram = AudioUtils.linearSpecgram(manager, samples, stride_ms, window_ms);
    // System.out.println(specgram.toDebugString(1000000000, 1000, 10, 1000));

    return specgram;
  }
}
