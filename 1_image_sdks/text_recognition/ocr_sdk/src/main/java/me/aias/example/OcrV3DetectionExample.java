package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageUtils;
import me.aias.example.utils.detection.OcrV3Detection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opencv.core.Mat;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * OCR文字检测(V3).
 * OCR Detection(V3)
 *
 * @author Calvin
 * @date 2022-07-23
 * @email 179209347@qq.com
 */
public final class OcrV3DetectionExample {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3DetectionExample.class);

    private OcrV3DetectionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/7.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        OcrV3Detection detection = new OcrV3Detection();
        try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
             Predictor<Image, NDList> detector = detectionModel.newPredictor()) {

            NDList dt_boxes = detector.predict(image);

            for (int i = 0; i < dt_boxes.size(); i++) {
                ImageUtils.drawRect((Mat) image.getWrappedImage(), dt_boxes.get(i));
            }
            ImageUtils.saveImage(image, "detect_rect.png", "build/output");
            ((Mat) image.getWrappedImage()).release();
        }
    }
}
