package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.face.detection.FaceDetModel;
import top.aias.face.detection.LightFaceDetModel;
import top.aias.face.feature.FaceFeature;
import top.aias.face.utils.FaceUtils;
import top.aias.face.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 人脸比对 - 1:1.
 * Face feature comparison - 1:1.
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FeatureComparisonExample {

    private static final Logger logger = LoggerFactory.getLogger(FeatureComparisonExample.class);

    private FeatureComparisonExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        Path imageFile1 = Paths.get("src/test/resources/kana1.jpg");
        Image img1 = ImageFactory.getInstance().fromFile(imageFile1);
        Path imageFile2 = Paths.get("src/test/resources/kana2.jpg");
        Image img2 = ImageFactory.getInstance().fromFile(imageFile2);

        try (FaceDetModel faceDetection = new FaceDetModel(Device.cpu());
             FaceFeature faceFeature = new FaceFeature(Device.cpu());) {

            DetectedObjects detections1 = faceDetection.predict(img1);
            List<DetectedObjects.DetectedObject> list = detections1.items();

//            ImageUtils.saveBoundingBoxImage(img1, detections1, "faces_detected_1.png", "build/output");

            // 只有一张人脸的情况，人脸可以进一步对112 * 112 图片按比例裁剪，去除冗余信息，比如头发等，以提高精度
            img1 = FaceUtils.align(img1, list.get(0).getBoundingBox());
            ImageUtils.saveImage(img1, "face_align_1.png", "build/output");
            float[] feature1 = faceFeature.predict(img1);
//            logger.info("face1 feature: " + Arrays.toString(feature1));

            DetectedObjects detections2 = faceDetection.predict(img2);
            List<DetectedObjects.DetectedObject> list2 = detections2.items();

//            ImageUtils.saveBoundingBoxImage(img2, detections2, "faces_detected_2.png", "build/output");

            img2 = FaceUtils.align(img2, list2.get(0).getBoundingBox());
            ImageUtils.saveImage(img2, "face_align_2.png", "build/output");
            float[] feature2 = faceFeature.predict(img2);
//            logger.info("face2 feature: " + Arrays.toString(feature2));

            logger.info("Similarity： " + faceFeature.calculSimilar(feature1, feature2));

        }
    }
}
