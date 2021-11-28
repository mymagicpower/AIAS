package me.aias.util;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FeatureComparison {

  private static final Logger logger = LoggerFactory.getLogger(FeatureComparison.class);

  private FeatureComparison() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile1 = Paths.get("src/test/resources/car1.png");
    Image img1 = ImageFactory.getInstance().fromFile(imageFile1);
    Path imageFile2 = Paths.get("src/test/resources/car2.png");
    Image img2 = ImageFactory.getInstance().fromFile(imageFile2);

    float[] feature1 = new FeatureExtraction().predict(img1);
    float[] feature2 = new FeatureExtraction().predict(img2);

    logger.info(Float.toString(calculSimilar(feature1, feature2)));
  }

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
    return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2) + 1) / 2.0f);
  }
}
