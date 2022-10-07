package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageUtils;
import me.aias.example.utils.opencv.OpenCVUtils;
import me.aias.example.utils.recognition.OcrV3AlignedRecognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR V3模型 文字识别. 要求图片是转正对齐的，不能有旋转角度
 *
 * @author Calvin
 * @date 2021-10-07
 * @email 179209347@qq.com
 */
public final class SimpleOcrV3RecognitionExample {

    private static final Logger logger = LoggerFactory.getLogger(SimpleOcrV3RecognitionExample.class);

    private SimpleOcrV3RecognitionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/ticket_0.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        OcrV3AlignedRecognition recognition = new OcrV3AlignedRecognition();
        try (ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
             Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
             ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
             Predictor<Image, String> recognizer = recognitionModel.newPredictor()) {

            long timeInferStart = System.currentTimeMillis();
            DetectedObjects detections = recognition.predict(image, detector, recognizer);
            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));

            List<DetectedObjects.DetectedObject> boxes = detections.items();
            for (DetectedObjects.DetectedObject result : boxes) {
                System.out.println(result.getClassName() + " : " + result.getProbability());
            }

            BufferedImage bufferedImage = OpenCVUtils.mat2Image((org.opencv.core.Mat) image.getWrappedImage());
            for (DetectedObjects.DetectedObject result : boxes) {
                Rectangle rect = result.getBoundingBox().getBounds();
                int width = image.getWidth();
                int height = image.getHeight();
                int x = (int) (rect.getX() * width);
                int y = (int) (rect.getY() * height);
                int rectWidth = (int) (rect.getWidth() * width);
                int rectHeight = (int) (rect.getHeight() * height);
                ImageUtils.drawImageRect(bufferedImage, x, y, rectWidth, rectHeight);
                ImageUtils.drawImageText(bufferedImage, result.getClassName(), x, y);
            }
            image = ImageFactory.getInstance().fromImage(OpenCVUtils.image2Mat(bufferedImage));
            ImageUtils.saveImage(image, "align_ocr_result.png", "build/output");

            //ImageUtils.saveBoundingBoxImage(image, detections, "ocr_result2.png", "build/output");
            logger.info("{}", detections);
        }
    }
}
