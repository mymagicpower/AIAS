package me.aias.example.model;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.detection.PpWordDetectionTranslator;
import me.aias.example.utils.recognition.PpWordRecognitionTranslator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 已摆正图片的文字识别
 *
 * @author Calvin
 * @date Oct 19, 2021
 */
public final class AlignedRecognitionModel implements AutoCloseable{
    private ZooModel<Image, DetectedObjects> detectionModel;
    private Predictor<Image, DetectedObjects> detector;
    private ZooModel<Image, String> recognitionModel;
    private Predictor<Image, String> recognizer;

    public void init(String detModel, String recModel) throws MalformedModelException, ModelNotFoundException, IOException {
        this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recModel));
        this.recognizer = recognitionModel.newPredictor();
        this.detectionModel = ModelZoo.loadModel(detectCriteria(detModel));
        this.detector = detectionModel.newPredictor();
    }

    public void close() {
        this.recognitionModel.close();
        this.recognizer.close();
        this.detectionModel.close();
        this.detector.close();
    }

    private Criteria<Image, DetectedObjects> detectCriteria(String detUri) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(detUri))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private Criteria<Image, String> recognizeCriteria(String recUri) {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get(recUri))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();

        return criteria;
    }

    // 多线程环境，需要把 Predictor<Image, DetectedObjects> detector 改写成线程池，每个线程一个predictor，共享一个model
    public synchronized String predictSingleLineText(Image image)
            throws TranslateException {
        return recognizer.predict(image);
    }

    // 多线程环境，需要把 Predictor<Image, DetectedObjects> detector / Predictor<Image, String> recognizer 改写成线程池，每个线程一个predictor，共享一个model
    public synchronized DetectedObjects predict(Image image)
            throws TranslateException {
        DetectedObjects detections = detector.predict(image);
        List<DetectedObjects.DetectedObject> boxes = detections.items();
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            Image subImg = getSubImage(image, boxes.get(i).getBoundingBox());
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }
            String name = recognizer.predict(subImg);
            System.out.println(name);
            names.add(name);
            prob.add(-1.0);
            rect.add(boxes.get(i).getBoundingBox());
        }
        DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);
        return detectedObjects;
    }

    private Image getSubImage(Image img, BoundingBox box) {
        Rectangle rect = box.getBounds();
        double[] extended = extendRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        int width = img.getWidth();
        int height = img.getHeight();
        int[] recovered = {
                (int) (extended[0] * width),
                (int) (extended[1] * height),
                (int) (extended[2] * width),
                (int) (extended[3] * height)
        };
        return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
    }

    private double[] extendRect(double xmin, double ymin, double width, double height) {
        double centerx = xmin + width / 2;
        double centery = ymin + height / 2;
        if (width > height) {
            width += height * 2.0;
            height *= 3.0;
        } else {
            height += width * 2.0;
            width *= 3.0;
        }
        double newX = centerx - width / 2 < 0 ? 0 : centerx - width / 2;
        double newY = centery - height / 2 < 0 ? 0 : centery - height / 2;
        double newWidth = newX + width > 1 ? 1 - newX : width;
        double newHeight = newY + height > 1 ? 1 - newY : height;
        return new double[]{newX, newY, newWidth, newHeight};
    }

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return OpenCVImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
