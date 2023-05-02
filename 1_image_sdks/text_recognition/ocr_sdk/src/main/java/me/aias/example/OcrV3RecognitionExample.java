package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDList;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageUtils;
import me.aias.example.utils.common.RotatedBox;
import me.aias.example.utils.common.RotatedBoxCompX;
import me.aias.example.utils.detection.OcrV3Detection;
import me.aias.example.utils.opencv.OpenCVUtils;
import me.aias.example.utils.recognition.OcrV3Recognition;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OCR V3模型 文字识别. 支持文本有旋转角度
 * OCR V3 model for text recognition. Supports text with rotation angles.
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

//            for (int i = 0; i < 1000; i++) {
//                detections = recognition.predict(image, detector, recognizer);
//                for (RotatedBox result : detections) {
//                    System.out.println(result.getText());
//                }
//                System.out.println("index : " + i);
//            }

            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));

//            for (RotatedBox result : detections) {
//                System.out.println(result.getText());
//            }

            // 对检测结果根据坐标位置，根据从上到下，从做到右，重新排序，下面算法对图片倾斜旋转角度较小的情形适用
            // 如果图片旋转角度较大，则需要自行改进算法，需要根据斜率校正计算位置。
            // Reorder the detection results based on the coordinate positions, from top to bottom, from left to right. The algorithm below is suitable for situations where the image is slightly tilted or rotated.
            // If the image rotation angle is large, the algorithm needs to be improved, and the position needs to be calculated based on the slope correction.
            List<RotatedBox> initList = new ArrayList<>();
            for (RotatedBox result : detections) {
                // put low Y value at the head of the queue.
                initList.add(result);
            }
            Collections.sort(initList);

            List<ArrayList<RotatedBoxCompX>> lines = new ArrayList<>();
            List<RotatedBoxCompX> line = new ArrayList<>();
            RotatedBoxCompX firstBox = new RotatedBoxCompX(initList.get(0).getBox(), initList.get(0).getText());
            line.add(firstBox);
            lines.add((ArrayList) line);
            for (int i = 1; i < initList.size(); i++) {
                RotatedBoxCompX tmpBox = new RotatedBoxCompX(initList.get(i).getBox(), initList.get(i).getText());
                float y1 = firstBox.getBox().toFloatArray()[1];
                float y2 = tmpBox.getBox().toFloatArray()[1];
                float dis = Math.abs(y2 - y1);
                if (dis < 32) { // 认为是同 1 行  - Considered to be in the same line
                    line.add(tmpBox);
                } else { // 换行 - Line break
                    firstBox = tmpBox;
                    Collections.sort(line);
                    line = new ArrayList<>();
                    line.add(firstBox);
                    lines.add((ArrayList) line);
                }
            }


            String fullText = "";
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).size(); j++) {
                    fullText += lines.get(i).get(j).getText() + "\t";
                }
                fullText += '\n';
            }

            System.out.println(fullText);


            // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
            Mat wrappedImage = (Mat) image.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            for (RotatedBox result : detections) {
                ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
            }

            Mat image2Mat = OpenCVUtils.image2Mat(bufferedImage);
            image = ImageFactory.getInstance().fromImage(image2Mat);
            ImageUtils.saveImage(image, "ocr_result.png", "build/output");

            wrappedImage.release();
            image2Mat.release();

            logger.info("{}", detections);
        }
    }
}
