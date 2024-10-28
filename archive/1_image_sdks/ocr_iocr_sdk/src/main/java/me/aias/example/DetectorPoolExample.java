package me.aias.example;


import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.detection.OCRDetectionTranslator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class DetectorPoolExample {
    /**
     * 文本检测
     *
     * @return
     */
    private static Criteria<Image, NDList> detectCriteria(String detUri) {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get(detUri))
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    public static void main(String[] args) throws ModelNotFoundException, MalformedModelException, IOException {
        Path imageFile = Paths.get("src/test/resources/template.png");
        Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);

        ZooModel<Image, NDList> detectionModel = ModelZoo.loadModel(detectCriteria("models/ch_PP-OCRv3_det_infer_onnx.zip"));

        int nThreads = 5; // 并发数量
        DetectorPool detectorPool = new DetectorPool(3, detectionModel);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads); // 3是线程池的大小

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                public void run() {
                    // 这里是需要异步执行的代码
                    try {
                        Predictor<Image, NDList> detector = detectorPool.getDetector();
                        NDList list = detector.predict(templateImg);
                        detectorPool.releaseDetector(detector);
                        System.out.println("" + index + ": "+ list.size());
                    } catch (TranslateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown(); // 当所有任务执行完毕后关闭线程池

    }
}