package top.aias.ocr;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.common.ImageUtils;
import top.aias.ocr.utils.common.RotatedBox;
import top.aias.ocr.utils.common.RotatedBoxCompX;
import top.aias.ocr.utils.detection.OcrV3Detection;
import top.aias.ocr.utils.opencv.OpenCVUtils;
import top.aias.ocr.utils.recognition.OcrV3Recognition;
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
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class OcrV3RecognitionExample {

    private OcrV3RecognitionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        Image image;
        String outPath = "";
        if (args.length < 2) {
            System.out.println("请输入图片路径");
            return;
        } else if (args.length == 2) {
            Path imageFile = Paths.get(args[1]);
            image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        } else if (args.length == 4) {
            Path imageFile = Paths.get(args[1]);
            image = OpenCVImageFactory.getInstance().fromFile(imageFile);
            outPath = args[3];
        } else {
            return;
        }

        String detModelUri ="E:\\desktop_app\\ocr\\models\\ch_PP-OCRv3_det_infer_onnx.zip";
        String recModelUri ="E:\\desktop_app\\ocr\\models\\ch_PP-OCRv3_rec_infer_onnx.zip";

        OcrV3Detection detection = new OcrV3Detection();
        OcrV3Recognition recognition = new OcrV3Recognition();
        try (ZooModel detectionModel = ModelZoo.loadModel(detection.chDetCriteria(detModelUri));
             Predictor<Image, NDList> detector = detectionModel.newPredictor();
             ZooModel recognitionModel = ModelZoo.loadModel(recognition.chRecCriteria(recModelUri));
             Predictor<Image, String> recognizer = recognitionModel.newPredictor();
             NDManager manager = NDManager.newBaseManager()) {

            long timeInferStart = System.currentTimeMillis();
            List<RotatedBox> detections = recognition.predict(manager, image, detector, recognizer);
            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));


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
                if (dis < 32) { // 认为是同 1 行
                    line.add(tmpBox);
                } else { // 换行
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

            System.err.print("AIAS " + fullText);

            BufferedImage bufferedImage = OpenCVUtils.mat2Image((org.opencv.core.Mat) image.getWrappedImage());
            for (RotatedBox result : detections) {
                ImageUtils.drawImageRect(bufferedImage, result.getBox());
            }
            image = OpenCVImageFactory.getInstance().fromImage(OpenCVUtils.image2Mat(bufferedImage));
//            ImageUtils.saveImage(image, "ocr_result.png", "/Users/calvin/Downloads/Easy_AI_Apps/backends_ocr/ocr_sdk/build/output");
            ImageUtils.saveImage(image, "ocr_result.png", outPath);
        }
    }
}
