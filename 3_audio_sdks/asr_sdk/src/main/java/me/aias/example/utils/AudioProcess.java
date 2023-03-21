package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对音频预处理的工具
 * Utility for audio preprocessing
 *
 */
public class AudioProcess {
  private static final Logger logger = LoggerFactory.getLogger(AudioProcess.class);

  public static NDArray processUtterance(NDManager manager, String path) throws Exception {
    // 获取音频的float数组
    // Get the float array of audio.
    float[] floatArray = AudioArrayUtils.audioSegment(path).samples;
    // System.out.println(Arrays.toString(floatArray));

    // 提取语音片段的特征
    // Extract features of the audio segment.
    NDArray specgram = AudioFeaturizer.featurize(manager, floatArray);

    // 使用均值和标准值计算音频特征的归一化值
    // Normalize the audio feature using mean and standard values.
    String npzDataPath = "src/test/resources/mean_std.npz";
    specgram = FeatureNormalizer.apply(manager, npzDataPath, specgram);
    // System.out.println(specgram.toDebugString(1000000000, 1000, 10, 1000));

    return specgram;
  }
}
