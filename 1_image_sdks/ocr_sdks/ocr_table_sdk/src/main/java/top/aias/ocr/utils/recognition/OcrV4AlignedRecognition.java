package top.aias.ocr.utils.recognition;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.detection.PpWordDetectionTranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Paths;
/**
 * 摆正的文本识别
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class OcrV4AlignedRecognition implements AutoCloseable {
    private Device device;
    ZooModel detModel;
    Predictor<Image, DetectedObjects> detector;
    ZooModel recModel;
    Predictor<Image, String> recognizer;


    public OcrV4AlignedRecognition(Device device) throws ModelException, IOException {
        this.device = device;
        this.detModel = ModelZoo.loadModel(detCriteria());
        this.detector = detModel.newPredictor();
        this.recModel = ModelZoo.loadModel(recCriteria());
        this.recognizer = recModel.newPredictor();
    }

    public void close(){
        this.detModel.close();
        this.detector.close();
        this.recModel.close();
        this.recognizer.close();
    }

    private Criteria<Image, DetectedObjects> detCriteria() {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv4_det_infer/inference.onnx"))
                        .optDevice(device)
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private Criteria<Image, String> recCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv4_rec_infer/inference.onnx"))
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();

        return criteria;
    }

    /**
     * 图片推理
     * @param image
     * @return
     * @throws TranslateException
     */
    public DetectedObjects predict(Image image) throws TranslateException {
        DetectedObjects detections = detector.predict(image);

        List<DetectedObjects.DetectedObject> boxes = detections.items();

        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        long timeInferStart = System.currentTimeMillis();
        for (int i = 0; i < boxes.size(); i++) {
            Image subImg = getSubImage(image, boxes.get(i).getBoundingBox());
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");
            String name = recognizer.predict(subImg);
            names.add(name);
            prob.add(-1.0);
            rect.add(boxes.get(i).getBoundingBox());
        }
        long timeInferEnd = System.currentTimeMillis();
        System.out.println("time: " + (timeInferEnd - timeInferStart));

        DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);

        return detectedObjects;
    }

    /**
     * 获取图片局部区域
     * @param img
     * @param box
     * @return
     */
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

    /**
     * 外扩图片
     * @param xmin
     * @param ymin
     * @param width
     * @param height
     * @return
     */
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

    /**
     * 旋转图片
     * @param image
     * @return
     */
    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
