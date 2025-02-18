package top.aias.platform.model.ocr;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.Point;
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
import org.opencv.core.Mat;
import top.aias.platform.bean.RotatedBox;
import top.aias.platform.utils.NDArrayUtils;
import top.aias.platform.utils.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private RecognizerPool recognizerPool;
    private String detModelPath;
    private String recModelPath;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public RecognitionModel(){}

    public RecognitionModel(String detModelPath, String recModelPath, int poolSize, Device device) {
        this.detModelPath = detModelPath;
        this.recModelPath = recModelPath;
        this.poolSize = poolSize;
        this.device = device;
    }


    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recModelPath));
                this.detectionModel = ModelZoo.loadModel(detectCriteria(detModelPath));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }

            this.detectorPool = new DetectorPool(poolSize, detectionModel);
            this.recognizerPool = new RecognizerPool(poolSize, recognitionModel);
            initialized.set(true);
        }
    }

    /**
     * 释放资源
     */
    public void close() {
        if (initialized.get()) {
            this.recognitionModel.close();
            this.detectionModel.close();
            this.detectorPool.close();
            this.recognizerPool.close();
        }
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
                        .optDevice(device)
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
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
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecTranslator((new ConcurrentHashMap<String, String>())))
                        .build();

        return criteria;
    }

    // 多线程环境，每个线程一个predictor，共享一个model, 资源池（CPU Core 核心数）达到上限则等待
    public List<RotatedBox> predict(NDManager manager, Image image)
            throws TranslateException {
        ensureInitialized();
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
            List<ai.djl.modality.cv.output.Point> srcPoints = new ArrayList<>();
            srcPoints.add(new ai.djl.modality.cv.output.Point(lt[0], lt[1]));
            srcPoints.add(new ai.djl.modality.cv.output.Point(rt[0], rt[1]));
            srcPoints.add(new ai.djl.modality.cv.output.Point(rb[0], rb[1]));
            srcPoints.add(new ai.djl.modality.cv.output.Point(lb[0], lb[1]));
            List<ai.djl.modality.cv.output.Point> dstPoints = new ArrayList<>();
            dstPoints.add(new ai.djl.modality.cv.output.Point(0, 0));
            dstPoints.add(new ai.djl.modality.cv.output.Point(img_crop_width, 0));
            dstPoints.add(new ai.djl.modality.cv.output.Point(img_crop_width, img_crop_height));
            dstPoints.add(new Point(0, img_crop_height));

            Mat srcPoint2f = NDArrayUtils.toMat(srcPoints);
            Mat dstPoint2f = NDArrayUtils.toMat(dstPoints);

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

    /**
     * 欧式距离计算
     *
     * @param point1
     * @param point2
     * @return
     */
    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    /**
     * 图片旋转
     *
     * @param manager
     * @param image
     * @return
     */
    private Image rotateImg(NDManager manager, Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return ImageFactory.getInstance().fromNDArray(rotated);
    }
}
