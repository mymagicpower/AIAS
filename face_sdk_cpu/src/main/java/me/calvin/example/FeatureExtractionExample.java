package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import me.calvin.face.FeatureExtraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 人脸特征提取
 * @author Calvin
 * @date 2021-06-09
 * @email 179209347@qq.com
 **/
public final class FeatureExtractionExample {

  private static final Logger logger = LoggerFactory.getLogger(FeatureExtractionExample.class);

  private FeatureExtractionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/kana1.jpg");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    float[] feature = FeatureExtraction.predict(img);
    if (feature != null) {
      logger.info(Arrays.toString(feature));
    }
  }
}
