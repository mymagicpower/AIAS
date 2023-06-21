package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.common.ImageUtils;
import me.aias.example.detection.OcrV3Detection;
import me.aias.example.opencv.OpenCVUtils;
import me.aias.example.recognition.OcrV3Recognition;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * OCR V3模型 文字识别
 * OCR V3 model for text recognition.
 *
 * @author Calvin
 * @date 2022-10-07
 * @email 179209347@qq.com
 */
public final class OcrV3RecognitionExample {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3RecognitionExample.class);

    private OcrV3RecognitionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/ticket_0.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        OcrV3Detection detection = new OcrV3Detection();
        OcrV3Recognition recognition = new OcrV3Recognition();
        try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
             Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
             ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
             Predictor<Image, String> recognizer = recognitionModel.newPredictor()) {

            long timeInferStart = System.currentTimeMillis();
            DetectedObjects detections = recognition.predict(image, detector, recognizer);

            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));

            List<DetectedObjects.DetectedObject> items = detections.items();

            // 转 BufferedImage 解决中文乱码问题
            Mat wrappedImage = (Mat) image.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            for (DetectedObjects.DetectedObject item : items) {
                Rectangle rectangle = item.getBoundingBox().getBounds();
                int x = (int) (rectangle.getX() * image.getWidth());
                int y = (int) (rectangle.getY() * image.getHeight());
                int width = (int) (rectangle.getWidth() * image.getWidth());
                int height = (int) (rectangle.getHeight() * image.getHeight());

                ImageUtils.drawImageRect(bufferedImage, x, y, width, height);
                ImageUtils.drawImageText(bufferedImage, item.getClassName(), x, y);
            }

            Mat image2Mat = OpenCVUtils.image2Mat(bufferedImage);
            image = OpenCVImageFactory.getInstance().fromImage(image2Mat);
            ImageUtils.saveImage(image, "ocr_result.png", "build/output");

            wrappedImage.release();
            image2Mat.release();

            logger.info("{}", detections);
        }
    }
}
