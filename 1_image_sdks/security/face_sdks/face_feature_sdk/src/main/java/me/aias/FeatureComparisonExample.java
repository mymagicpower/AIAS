package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.util.FaceFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 人脸比对 - 1:1.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FeatureComparisonExample {

    private static final Logger logger = LoggerFactory.getLogger(FeatureComparisonExample.class);

    private FeatureComparisonExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        Path imageFile1 = Paths.get("src/test/resources/kana1.jpg");
        Image img1 = ImageFactory.getInstance().fromFile(imageFile1);
        Path imageFile2 = Paths.get("src/test/resources/kana2.jpg");
        Image img2 = ImageFactory.getInstance().fromFile(imageFile2);

        FaceFeature faceFeature = new FaceFeature();
        try (ZooModel<Image, float[]> model = ModelZoo.loadModel(faceFeature.criteria());
             Predictor<Image, float[]> predictor = model.newPredictor()) {

            float[] feature1 = predictor.predict(img1);
            logger.info("face1 feature: " + Arrays.toString(feature1));
            float[] feature2 = predictor.predict(img2);
            logger.info("face2 feature: " + Arrays.toString(feature2));

            logger.info("相似度： "+ Float.toString(faceFeature.calculSimilar(feature1, feature2)));
        }
    }
}
