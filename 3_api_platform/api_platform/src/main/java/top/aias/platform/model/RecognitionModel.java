package top.aias.platform.model;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
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
import top.aias.platform.bean.Point;
import top.aias.platform.bean.RotatedBox;
import top.aias.platform.pool.ocr.DetectorPool;
import top.aias.platform.pool.ocr.HorizontalDetectorPool;
import top.aias.platform.pool.ocr.RecognizerPool;
import top.aias.platform.translator.ocr.OcrDetectionTranslator;
import top.aias.platform.translator.ocr.PpWordRecognitionTranslator;
import top.aias.platform.utils.OpenCVUtils;
import org.opencv.core.Mat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字识别模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class RecognitionModel implements AutoCloseable {
    private ZooModel<Image, NDList> detectionModel;
    private ZooModel<Image, String> recognitionModel;

    private DetectorPool detectorPool;
    private HorizontalDetectorPool horizontalDetectorPool;
    private RecognizerPool recognizerPool;

    public void init(String detModel, String recModel, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recModel));
        this.detectionModel = ModelZoo.loadModel(detectCriteria(detModel));

        this.detectorPool = new DetectorPool(poolSize, detectionModel);
        this.recognizerPool = new RecognizerPool(poolSize, recognitionModel);

    }
    /**
     * 释放资源
     */
    public void close() {
        this.recognitionModel.close();
        this.detectionModel.close();
        this.detectorPool.close();
        this.horizontalDetectorPool.close();
        this.recognizerPool.close();
    }

    /**
     * 文本检测（支持有倾斜角的文本）
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
//                        .optModelUrls(detUri)
                        .optTranslator(new OcrDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

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
//                        .optModelUrls(recUri)
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();

        return criteria;
    }

    // 多线程环境，每个线程一个predictor，共享一个model, 资源池（CPU Core 核心数）达到上限则等待
    public String predictSingleLineText(Image image)
            throws TranslateException {
        Predictor<Image, String> recognizer = recognizerPool.getRecognizer();
        String text = recognizer.predict(image);
        // 释放资源
        recognizerPool.releaseRecognizer(recognizer);
        return text;
    }

    // 多线程环境，每个线程一个predictor，共享一个model, 资源池（CPU Core 核心数）达到上限则等待
    public DetectedObjects predict(Image image)
            throws TranslateException {
        Predictor<Image, DetectedObjects> horizontalDetector = horizontalDetectorPool.getDetector();
        DetectedObjects detections = horizontalDetector.predict(image);
        horizontalDetectorPool.releaseDetector(horizontalDetector);

        List<DetectedObjects.DetectedObject> boxes = detections.items();
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        Predictor<Image, String> recognizer = recognizerPool.getRecognizer();
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
        // 释放资源
        recognizerPool.releaseRecognizer(recognizer);

        DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);
        return detectedObjects;
    }

    // 多线程环境，每个线程一个predictor，共享一个model, 资源池（CPU Core 核心数）达到上限则等待
    public List<RotatedBox> predict(NDManager manager, Image image)
            throws TranslateException {

        Predictor<Image, NDList> detector = detectorPool.getDetector();
        NDList boxes = detector.predict(image);
        // 释放资源
        detectorPool.releaseDetector(detector);

        // 交给 NDManager自动管理内存
        // attach to manager for automatic memory management
        boxes.attach(manager);

        List<RotatedBox> result = new ArrayList<>();
        Mat mat = (Mat) image.getWrappedImage();

        Predictor<Image, String> recognizer = recognizerPool.getRecognizer();
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
        // 释放资源
        recognizerPool.releaseRecognizer(recognizer);

        return result;
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

    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return OpenCVImageFactory.getInstance().fromNDArray(rotated);
        }
    }

    private Image rotateImg(NDManager manager, Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return OpenCVImageFactory.getInstance().fromNDArray(rotated);
    }
}
