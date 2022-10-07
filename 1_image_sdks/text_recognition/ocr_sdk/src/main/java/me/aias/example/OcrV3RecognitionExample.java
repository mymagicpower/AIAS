package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDList;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageUtils;
import me.aias.example.utils.common.RotatedBox;
import me.aias.example.utils.detection.OcrV3Detection;
import me.aias.example.utils.opencv.OpenCVUtils;
import me.aias.example.utils.recognition.OcrV3AlignedRecognition;
import me.aias.example.utils.recognition.OcrV3Recognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * OCR V3模型 文字识别. 支持文本有旋转角度
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
        Path imageFile = Paths.get("src/test/resources/7.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        OcrV3Detection detection = new OcrV3Detection();
        OcrV3Recognition recognition = new OcrV3Recognition();
        try (ZooModel detectionModel = ModelZoo.loadModel(detection.detectCriteria());
             Predictor<Image, NDList> detector = detectionModel.newPredictor();
             ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
             Predictor<Image, String> recognizer = recognitionModel.newPredictor()) {

            long timeInferStart = System.currentTimeMillis();
            List<RotatedBox> detections = recognition.predict(image, detector, recognizer);
            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));

            for (RotatedBox result : detections) {
                System.out.println(result.getText());
            }

            BufferedImage bufferedImage = OpenCVUtils.mat2Image((org.opencv.core.Mat) image.getWrappedImage());
            for (RotatedBox result : detections) {
                ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
            }
            image = ImageFactory.getInstance().fromImage(OpenCVUtils.image2Mat(bufferedImage));
            ImageUtils.saveImage(image, "ocr_result.png", "build/output");
            logger.info("{}", detections);
        }
    }
}
