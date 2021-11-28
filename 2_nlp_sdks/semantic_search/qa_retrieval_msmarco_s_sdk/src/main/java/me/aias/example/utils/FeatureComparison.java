package me.aias.example.utils;

public final class FeatureComparison {
  private FeatureComparison() {}

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
