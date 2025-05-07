package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.MatOfPoint2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.face.detection.FaceDetection;
import top.aias.face.quality.FaceFeature;
import top.aias.face.utils.FaceUtils;
import top.aias.face.utils.FeatureComparison;
import top.aias.face.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸比对
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class FeatureComparisonExample {
    private static final Logger logger = LoggerFactory.getLogger(FeatureComparisonExample.class);

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path path1 = Paths.get("src/test/resources/face_recognition_1.png");
        Path path2 = Paths.get("src/test/resources/face_recognition_2.png");
        Image image1 = new OpenCVImageFactory().fromFile(path1);
        Image image2 = new OpenCVImageFactory().fromFile(path2);

        try (FaceDetection faceDetection = new FaceDetection(Device.cpu());
             FaceFeature faceFeature = new FaceFeature(Device.cpu())) {

            DetectedObjects detections1 = faceDetection.predict(image1);
            DetectedObjects.DetectedObject result1 = detections1.item(0);
            BoundingBox box1 = result1.getBoundingBox();
            Image alignImg1 = FaceUtils.align(image1, box1);
            ImageUtils.saveImage(alignImg1, "face_align_1.png", "build/output");
            float[] feature1 = faceFeature.predict(alignImg1);

            DetectedObjects detections2 = faceDetection.predict(image2);

            DetectedObjects.DetectedObject result2 = detections2.item(0);
            BoundingBox box2 = result2.getBoundingBox();
            Image alignImg2 = FaceUtils.align(image2, box2);
            ImageUtils.saveImage(alignImg2, "face_align_2.png", "build/output");
            float[] feature2 = faceFeature.predict(alignImg2);

            // 余弦相似度
            float cos = FeatureComparison.cosineSim(feature1, feature2);
            logger.info(Float.toString(cos));

//            ImageUtils.saveBoundingBoxImage(image2, detections2, "faces_detected.png", "build/output");
        }

    }
}