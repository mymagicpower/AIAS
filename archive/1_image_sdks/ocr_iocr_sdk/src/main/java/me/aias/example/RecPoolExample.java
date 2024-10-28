package me.aias.example;


import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDList;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.model.RecognitionModel;
import me.aias.example.utils.common.DJLImageUtils;
import me.aias.example.utils.common.LabelBean;
import me.aias.example.utils.common.PointUtils;
import me.aias.example.utils.detection.OCRDetectionTranslator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecPoolExample {

    public static void main(String[] args) throws ModelNotFoundException, MalformedModelException, IOException {
        Path imageFile = Paths.get("src/test/resources/template.png");
        Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);

        int nThreads = 5; // 并发数量

        RecognitionModel recognitionModel = new RecognitionModel();
        recognitionModel.init("models/ch_PP-OCRv3_det_infer_onnx.zip", "models/ch_PP-OCRv3_rec_infer_onnx.zip", 4);

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads); // 3是线程池的大小

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                public void run() {
                    // 这里是需要异步执行的代码
                    try {
                        DetectedObjects textDetections = recognitionModel.predict(templateImg);
                        List<DetectedObjects.DetectedObject> dt_boxes = textDetections.items();
                        for (DetectedObjects.DetectedObject item : dt_boxes) {
                            System.out.println(item.getClassName());
                        }
                    } catch (TranslateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown(); // 当所有任务执行完毕后关闭线程池

    }
}