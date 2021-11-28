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
 *
 * @author Calvin <179209347@qq.com>
 */
public class FeatureNormalizer {
  static float eps = 1e-20f; // 添加到标准值以提供数值稳定性
  /**
   * 使用均值和标准值计算音频特征的归一化值
   *
   * @param manager
   * @param npzDataPath: 均值和标准值的文件路径
   * @param features: 需要归一化的音频
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
