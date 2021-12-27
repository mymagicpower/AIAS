package me.aias.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 特征向量工具类
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public final class FeatureUtils {
    private FeatureUtils() {
    }

    public static List<Float> normalizer(double[] arr) {
        double sum = 0.0f;
        List<Float> normFeature = new ArrayList<>();
        for (int i = 0; i < arr.length; ++i) {
            sum += Math.pow((float) arr[i], 2);
        }
        sum = Math.sqrt(sum);
        for (int i = 0; i < arr.length; ++i) {
            normFeature.add((float) arr[i] / (float) sum);
        }
        return normFeature;
    }

    public static List<Float> normalizer(List<Float> feature) {
        double sum = 0.0f;
        int length = feature.size();
        List<Float> normFeature = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            sum += Math.pow(feature.get(i), 2);
        }
        sum = Math.sqrt(sum);
        for (int i = 0; i < length; ++i) {
            normFeature.add(feature.get(i) / (float) sum);
        }
        return normFeature;
    }

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
