package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    //https://github.com/deepjavalibrary/djl/blob/master/api/src/test/java/ai/djl/ndarray/NDSerializerTest.java
    //https://github.com/deepjavalibrary/djl/blob/master/api/src/test/java/ai/djl/ndarray/NDListTest.java
    byte[] data = Files.readAllBytes(Paths.get(npzDataPath));
    NDList decoded = NDList.decode(manager, data);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length + 1);
    decoded.encode(bos, true);
    NDList list = NDList.decode(manager, bos.toByteArray());
    NDArray meanNDArray = list.get(0);//mean
    meanNDArray = meanNDArray.toType(DataType.FLOAT32, false);
    NDArray stdNDArray = list.get(1);//std
    stdNDArray = stdNDArray.toType(DataType.FLOAT32, false);

    // (features - self._mean) / (self._std + eps)
    stdNDArray = stdNDArray.add(eps);
    features = features.sub(meanNDArray).div(stdNDArray);
    return features;
  }
}
