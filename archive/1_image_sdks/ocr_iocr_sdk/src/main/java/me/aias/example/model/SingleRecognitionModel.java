package me.aias.example.model;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.Point;
import me.aias.example.utils.common.RotatedBox;
import me.aias.example.utils.detection.OCRDetectionTranslator;
import me.aias.example.utils.opencv.OpenCVUtils;
import me.aias.example.utils.recognition.PpWordRecognitionTranslator;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public final class SingleRecognitionModel implements AutoCloseable {
    private ZooModel<Image, NDList> detectionModel;
    private Predictor<Image, NDList> detector;
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

    /**
     * 文本检测
     *
     * @return
     */
    private Criteria<Image, NDList> detectCriteria(String detUri) {
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

//    private Criteria<Image, DetectedObjects> detectCriteria(String detUri) {
//        Criteria<Image, DetectedObjects> criteria =
//                Criteria.builder()
//                        .optEngine("OnnxRuntime")
//                        .optModelName("inference")
//                        .setTypes(Image.class, DetectedObjects.class)
//                        .optModelPath(Paths.get(detUri))
//                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
//                        .optProgress(new ProgressBar())
//                        .build();
//
//        return criteria;
//    }

    /**
     * 文本识别
     *
     * @return
     */
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
    public synchronized List<RotatedBox> predict(NDManager manager, Image image)
            throws TranslateException {
        NDList boxes = detector.predict(image);
        // 交给 NDManager自动管理内存
        // attach to manager for automatic memory management
        boxes.attach(manager);

        List<RotatedBox> result = new ArrayList<>();
        Mat mat = (Mat) image.getWrappedImage();

        for (int i = 0; i < boxes.size(); i++) {
            NDArray box = boxes.get(i);

            float[] pointsArr = box.toFloatArray();
            float[] lt = java.util.Arrays.copyOfRange(pointsArr, 0, 2);
            float[] rt = java.util.Arrays.copyOfRange(pointsArr, 2, 4);
            float[] rb = java.util.Arrays.copyOfRange(pointsArr, 4, 6);
            float[] lb = java.util.Arrays.copyOfRange(pointsArr, 6, 8);
            int img_crop_width = (int) Math.max(distance(lt, rt), distance(rb, lb));
            int img_crop_height = (int) Math.max(distance(lt, lb), distance(rt, rb));
            List<Point> srcPoints = new ArrayList<>();
            srcPoints.add(new Point((int) lt[0], (int) lt[1]));
            srcPoints.add(new Point((int) rt[0], (int) rt[1]));
            srcPoints.add(new Point((int) rb[0], (int) rb[1]));
            srcPoints.add(new Point((int) lb[0], (int) lb[1]));
            List<Point> dstPoints = new ArrayList<>();
            dstPoints.add(new Point(0, 0));
            dstPoints.add(new Point(img_crop_width, 0));
            dstPoints.add(new Point(img_crop_width, img_crop_height));
            dstPoints.add(new Point(0, img_crop_height));

            Mat srcPoint2f = OpenCVUtils.toMat(srcPoints);
            Mat dstPoint2f = OpenCVUtils.toMat(dstPoints);

            Mat cvMat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);

            Image subImg = OpenCVImageFactory.getInstance().fromImage(cvMat);
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");

            subImg = subImg.getSubImage(0, 0, img_crop_width, img_crop_height);
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(manager, subImg);
            }

            String name = recognizer.predict(subImg);
            RotatedBox rotatedBox = new RotatedBox(box, name);
            result.add(rotatedBox);

            cvMat.release();
            srcPoint2f.release();
            dstPoint2f.release();

        }
        return result;
    }

    private BufferedImage get_rotate_crop_image(Image image, NDArray box) {
        return null;
    }

    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    private Image rotateImg(NDManager manager, Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return OpenCVImageFactory.getInstance().fromNDArray(rotated);
    }
}
