package me.aias;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.util.ImageEncoderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 图片特征提取 （512维特征）
 * Image feature extraction (512-dimensional features)
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
        Path imageFile = Paths.get("src/test/resources/car1.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        ImageEncoderModel imageEncoderModel = new ImageEncoderModel();
        imageEncoderModel.init("models/CLIP-ViT-B-32-IMAGE.pt", 4);

        float[] feature = imageEncoderModel.predict(img);
        System.out.println(feature.length);
        logger.info(Arrays.toString(feature));
    }
}
