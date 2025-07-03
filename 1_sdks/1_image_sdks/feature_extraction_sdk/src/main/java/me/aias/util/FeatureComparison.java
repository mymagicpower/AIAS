package me.aias.util;

/**
 * 特征相似度计算
 * https://zhuanlan.zhihu.com/p/88117781?utm_source=wechat_session
 * Feature Similarity Calculation
 * @author Calvin
 * @date 2021-07-10
 * @email 179209347@qq.com
 **/
public final class FeatureComparison {
  private FeatureComparison() {}

  /**
   * 余弦相似度
   * Cosine similarity
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

  /**
   * 欧式距离
   * Euclidean distance
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
   * Inner product
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
