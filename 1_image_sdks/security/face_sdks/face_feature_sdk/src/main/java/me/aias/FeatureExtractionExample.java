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
 * 人脸特征提取
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FeatureExtractionExample {

    private static final Logger logger = LoggerFactory.getLogger(FeatureExtractionExample.class);

    private FeatureExtractionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/kana1.jpg");
        Image img = ImageFactory.getInstance().fromFile(imageFile);

        FaceFeature faceFeature = new FaceFeature();
        try (ZooModel<Image, float[]> model = ModelZoo.loadModel(faceFeature.criteria());
             Predictor<Image, float[]> predictor = model.newPredictor()) {
            float[] feature = predictor.predict(img);
            if (feature != null) {
                logger.info("Face feature: " + Arrays.toString(feature));
            }
        }
    }
}
