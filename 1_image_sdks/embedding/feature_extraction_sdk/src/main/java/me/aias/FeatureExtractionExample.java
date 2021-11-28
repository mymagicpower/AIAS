package me.aias;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.util.FeatureExtraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 图片特征提取 （512维特征）
 *
 * @author Calvin
 * @date 2021-07-10
 * @email 179209347@qq.com
 */
public final class FeatureExtractionExample {

    private static final Logger logger = LoggerFactory.getLogger(FeatureExtractionExample.class);

    private FeatureExtractionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/car1.png");
        Image img = ImageFactory.getInstance().fromFile(imageFile);

        float[] feature = new FeatureExtraction().predict(img);

        System.out.println(feature.length);
        if (feature != null) {
            logger.info(Arrays.toString(feature));
        }
    }
}
