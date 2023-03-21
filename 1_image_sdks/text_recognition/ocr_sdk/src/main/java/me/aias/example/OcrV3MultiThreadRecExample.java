package me.aias.example;

import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageUtils;
import me.aias.example.utils.recognition.OcrV3MultiThreadRecognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * OCR V3模型 多线程文字识别.
 * OCR V3 model multi-threaded text recognition.
 * 由于底层引擎原因，与其它引擎可以共享一个模型不同，paddle多线程需要加载多个模型。
 * Due to engine limitations, unlike other engines that can share a model, paddle multi-threading requires loading multiple models.
 ***************************************************************************
 *   请参考 OcrV3RecognitionExample 更新检测与识别部分，这个例子只是演示多线程的使用 *
 *   Please refer to OcrV3RecognitionExample to update the detection and recognition parts, this example only demonstrates the use of multi-threading *
 ***************************************************************************
 * @author Calvin
 * @date 2022-07-24
 * @email 179209347@qq.com
 */
public final class OcrV3MultiThreadRecExample {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3MultiThreadRecExample.class);

    private OcrV3MultiThreadRecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/ticket_0.png");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        // 并发线程数，最大上限为 CPU 核数
        // Concurrent threads, with a maximum limit of CPU cores
        int threadNum = 4;

        OcrV3MultiThreadRecognition recognition = new OcrV3MultiThreadRecognition();
        try (ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
             Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor()) {
            // 由于底层引擎原因，与其它引擎可以共享一个模型不同，paddle多线程需要每个线程加载一个模型
            // Due to engine limitations, unlike other engines that can share a model, paddle multi-threading requires each thread to load a model
            // 可以将paddle模型转换成ONNX，ONNX底层引擎支持的更好
            // Paddle models can be converted to ONNX, which is better supported by the ONNX engine.
            List<ZooModel> recModels = new ArrayList<>();
            try {
                for (int i = 0; i < threadNum; i++) {
                    ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
                    recModels.add(recognitionModel);
                }

                long timeInferStart = System.currentTimeMillis();
                DetectedObjects detections = recognition.predict(image, recModels, detector, threadNum);
                long timeInferEnd = System.currentTimeMillis();
                System.out.println("time: " + (timeInferEnd - timeInferStart));

                List<DetectedObjects.DetectedObject> boxes = detections.items();
                for (DetectedObjects.DetectedObject result : boxes) {
                    System.out.println(result.getClassName() + " : " + result.getProbability());
                }

                ImageUtils.saveBoundingBoxImage(image, detections, "ocr_result.png", "build/output");
                logger.info("{}", detections);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            } finally {
                for (ZooModel recognitionModel : recModels) {
                    recognitionModel.close();
                }
            }


        }
    }
}
