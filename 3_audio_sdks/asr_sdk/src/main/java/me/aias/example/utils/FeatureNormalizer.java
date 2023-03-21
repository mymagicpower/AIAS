package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * 对音频预处理的工具
 * Tool for pre-processing audio features
 *
 * @author Calvin <179209347@qq.com>
 */
public class FeatureNormalizer {
  static float eps = 1e-20f; // 添加到标准值以提供数值稳定性 - Added to the standard value for numerical stability
  /**
   * 使用均值和标准值计算音频特征的归一化值
   * Computes the normalized value of audio features using mean and standard deviation
   *
   * @param manager
   * @param npzDataPath: 均值和标准值的文件路径 - File path for mean and standard deviation
   * @param features: 需要归一化的音频 - Audio features to be normalized
   * @return
   * @throws Exception
   */
  public static NDArray apply(NDManager manager, String npzDataPath, NDArray features)
      throws Exception {
    File file = new File(npzDataPath);
    Map<String, INDArray> map = Nd4j.createFromNpzFile(file);
    INDArray meanArray = map.get("mean");
    float[][] mean = meanArray.toFloatMatrix();
    NDArray meanNDArray = manager.create(mean);
    INDArray stdArray = map.get("std");
    float[][] std = stdArray.toFloatMatrix();
    NDArray stdNDArray = manager.create(std);
    // (features - self._mean) / (self._std + eps)
    stdNDArray = stdNDArray.add(eps);
    features = features.sub(meanNDArray).div(stdNDArray);
    return features;
  }
}
