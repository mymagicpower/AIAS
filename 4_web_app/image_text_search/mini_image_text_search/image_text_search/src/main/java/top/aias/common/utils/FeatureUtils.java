package top.aias.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * 特征比对工具类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
public final class FeatureUtils {
  private FeatureUtils() {}

  /**
   * 余弦相似度
   * @param feature1
   * @param feature2
   * @return
   */
  public static float cosineSim(float[] feature1, float[] feature2) {
    float ret = 0.0f;
    float mod1 = 0.0f;
    float mod2 = 0.0f;
    int length = feature1.length;
    for (int i = 0; i < length; ++i) {
      ret += feature1[i] * feature2[i];
      mod1 += feature1[i] * feature1[i];
      mod2 += feature2[i] * feature2[i];
    }
    //    dot(x, y) / (np.sqrt(dot(x, x)) * np.sqrt(dot(y, y))))
    return (float) (ret / Math.sqrt(mod1) / Math.sqrt(mod2));
  }

  public static float[] softmax(float[] sims) {
    List<Float> list = Arrays.asList(ArrayUtils.toObject(sims));
    float max = Collections.max(list);

    float[] result = new float[sims.length];
    float sum = 0f;
    for (float value : sims) {
      sum = sum + (float) Math.exp(value - max);
    }

    for (int i = 0; i < sims.length; i++) {
      result[i] = (float) Math.exp(sims[i] - max) / sum;
    }
    return result;
  }

  /**
   * 欧式距离
   * @param feature1
   * @param feature2
   * @return
   */
  public static float dis(float[] feature1, float[] feature2) {
    float sum = 0.0f;
    int length = feature1.length;
    for (int i = 0; i < length; ++i) {
      sum += Math.pow(feature1[i] - feature2[i], 2);
    }
    return (float) Math.sqrt(sum);
  }

  /**
   * 内积
   * @param feature1
   * @param feature2
   * @return
   */
  public static float dot(float[] feature1, float[] feature2) {
    float ret = 0.0f;
    int length = feature1.length;
    // dot(x, y)
    for (int i = 0; i < length; ++i) {
      ret += feature1[i] * feature2[i];
    }

    return ret;
  }
}
