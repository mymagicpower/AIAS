package me.aias.example.utils;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class FeatureUtils {

  public static float calculSimilar(float[] feature1, float[] feature2) {
    float ret = 0.0f;
    float mod1 = 0.0f;
    float mod2 = 0.0f;
    int length = feature1.length;
    for (int i = 0; i < length; ++i) {
      ret += feature1[i] * feature2[i];
      mod1 += feature1[i] * feature1[i];
      mod2 += feature2[i] * feature2[i];
    }
    return (float) (ret / Math.sqrt(mod1) / Math.sqrt(mod2));
  }
}
