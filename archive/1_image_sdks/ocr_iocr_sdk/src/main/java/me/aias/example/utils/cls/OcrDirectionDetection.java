package me.aias.example.utils.cls;

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
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.DirectionInfo;
import me.aias.example.utils.detection.PpWordDetectionTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class OcrDirectionDetection {

    private static final Logger logger = LoggerFactory.getLogger(OcrDirectionDetection.class);

    public OcrDirectionDetection() {
    }

    public DetectedObjects predict(
            Image image,
            Predictor<Image, DetectedObjects> detector,
            Predictor<Image, DirectionInfo> rotateClassifier)
            throws TranslateException {
        DetectedObjects detections = detector.predict(image);

        List<DetectedObjects.DetectedObject> boxes = detections.items();

        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        for (int i = 0; i < boxes.size(); i++) {
            Image subImg = getSubImage(image, boxes.get(i).getBoundingBox());
            DirectionInfo result = null;
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
                result = rotateClassifier.predict(subImg);
                prob.add(result.getProb());
                if (result.getName().equalsIgnoreCase("Rotate")) {
                    names.add("90");
                } else {
                    names.add("270");
                }
            } else {
                result = rotateClassifier.predict(subImg);
                prob.add(result.getProb());
                if (result.getName().equalsIgnoreCase("No Rotate")) {
                    names.add("0");
                } else {
                    names.add("180");
                }
            }
            rect.add(boxes.get(i).getBoundingBox());
        }
        DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);

        return detectedObjects;
    }

    public Criteria<Image, DetectedObjects> detectCriteria() {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv2_det_infer_onnx.zip"))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    public Criteria<Image, DirectionInfo> clsCriteria() {

        Criteria<Image, DirectionInfo> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DirectionInfo.class)
                        .optModelPath(Paths.get("models/ch_ppocr_mobile_v2.0_cls_onnx.zip"))
                        .optTranslator(new PpWordRotateTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
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
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}