package top.aias.ocr.utils.cls;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.ocr.utils.common.DirectionInfo;
import top.aias.ocr.utils.common.RotatedBox;
import top.aias.ocr.utils.detection.OCRDetectionTranslator;
import top.aias.ocr.utils.opencv.NDArrayUtils;
import top.aias.ocr.utils.opencv.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字方向检测
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class OcrDirectionDetection implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(OcrDirectionDetection.class);
    private ZooModel detModel;
    private Predictor<Image, NDList> detector;
    private ZooModel clsModel;
    private Predictor<Image, DirectionInfo> rotateClassifier;
    private NDManager manager;
    private Device device;

    public OcrDirectionDetection(Device device) throws ModelException, IOException {
        this.device = device;
        this.detModel = ModelZoo.loadModel(detCriteria());
        this.detector = detModel.newPredictor();
        this.clsModel = ModelZoo.loadModel(clsCriteria());
        this.rotateClassifier = clsModel.newPredictor();
        this.manager = NDManager.newBaseManager();
    }

    private Criteria<Image, NDList> detCriteria() {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
//                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv4_det_infer.onnx"))
                        .optDevice(device)
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private Criteria<Image, DirectionInfo> clsCriteria() {

        Criteria<Image, DirectionInfo> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(Image.class, DirectionInfo.class)
                        .optModelPath(Paths.get("models/ch_ppocr_mobile_v2.0_cls.onnx"))
                        .optDevice(device)
                        .optTranslator(new PpWordRotateTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

    public void close() {
        this.detModel.close();
        this.detector.close();
        this.clsModel.close();
        this.rotateClassifier.close();
        this.manager.close();
    }

    public List<RotatedBox> predict(Image image) throws TranslateException {
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
            srcPoints.add(new Point(lt[0], lt[1]));
            srcPoints.add(new Point(rt[0], rt[1]));
            srcPoints.add(new Point(rb[0], rb[1]));
            srcPoints.add(new Point(lb[0], lb[1]));
            List<Point> dstPoints = new ArrayList<>();
            dstPoints.add(new Point(0, 0));
            dstPoints.add(new Point(img_crop_width, 0));
            dstPoints.add(new Point(img_crop_width, img_crop_height));
            dstPoints.add(new Point(0, img_crop_height));

            Mat srcPoint2f = NDArrayUtils.toMat(srcPoints);
            Mat dstPoint2f = NDArrayUtils.toMat(dstPoints);

            Mat cvMat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);

            Image subImg = OpenCVImageFactory.getInstance().fromImage(cvMat);
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");

            subImg = subImg.getSubImage(0, 0, img_crop_width, img_crop_height);
            DirectionInfo directionInfo = null;
            String angle;
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
                directionInfo = rotateClassifier.predict(subImg);
                if (directionInfo.getName().equalsIgnoreCase("Rotate")) {
                    angle = "90";
                } else {
                    angle = "270";
                }
                RotatedBox rotatedBox = new RotatedBox(box, angle, directionInfo.getProb());
                result.add(rotatedBox);
            } else {
                directionInfo = rotateClassifier.predict(subImg);
                if (directionInfo.getName().equalsIgnoreCase("No Rotate")) {
                    angle = "0";
                } else {
                    angle = "180";
                }
                RotatedBox rotatedBox = new RotatedBox(box, angle, directionInfo.getProb());
                result.add(rotatedBox);
            }


            cvMat.release();
            srcPoint2f.release();
            dstPoint2f.release();

        }

        return result;
    }

    /**
     * 图片旋转
     *
     * @param image
     * @return
     */
    private Image rotateImg(Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return ImageFactory.getInstance().fromNDArray(rotated);
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
}