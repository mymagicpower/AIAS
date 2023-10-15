package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.utils.FaceDetection;
import me.aias.utils.FaceLandmark;
import me.aias.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class FaceLandmarkExample {

    private static final Logger logger = LoggerFactory.getLogger(FaceLandmarkExample.class);

    private FaceLandmarkExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/faces.jpg");
        Image image = ImageFactory.getInstance().fromFile(imageFile);

        // 图像缩放比，越小速度越快，精度越低，需根据场景调优
        // Scaling factor for the image, smaller is faster but less accurate, tune for your use case
        float scale = 0.5f;
        // 检测结果的置信度过滤阈值
        // Confidence threshold to filter detection results
        float threshold = 0.7f;
        FaceDetection faceDetection = new FaceDetection();
        FaceLandmark faceLandmark = new FaceLandmark();
        try (ZooModel<Image, DetectedObjects> model =
                     ModelZoo.loadModel(faceDetection.criteria(scale, threshold));
             Predictor<Image, DetectedObjects> faceDetector = model.newPredictor();
             ZooModel classifyModel = ModelZoo.loadModel(faceLandmark.criteria());
             Predictor<Image, float[][]> landmarkDetector = classifyModel.newPredictor()) {

            DetectedObjects detections = faceDetector.predict(image);
            List<DetectedObjects.DetectedObject> faces = detections.items();
            //    List<String> names = new ArrayList<>();
            //    List<Double> prob = new ArrayList<>();
            //    List<BoundingBox> rect = new ArrayList<>();

            int index = 0;

            for (DetectedObjects.DetectedObject face : faces) {
                // names.add(face.getClassName());
                // prob.add(face.getProbability());
                // rect.add(face.getBoundingBox());

                // 外扩人脸比例 factor = 1, 100%, factor = 0.2, 20%
                // Expand the ratio of the face outward: factor = 1, 100%; factor = 0.2, 20%
                Image subImg = ImageUtils.getSubImage(image, face.getBoundingBox(), 0f);
                ImageUtils.saveImage(subImg, "face_" + index++ + ".png", "build/output");
                float[][] result = landmarkDetector.predict(subImg);

                logger.info("{}", Arrays.toString(result[0]));
                ImageUtils.drawLandmark(image, face.getBoundingBox(), result[0]);

            }
            logger.info(
                    " Face landmarks detection result image has been saved in: {}",
                    "build/output/face_landmarks.png");
            ImageUtils.saveBoundingBoxImage(image, detections, "face_landmarks.png", "build/output");

            logger.info("{}", detections);
        }
    }
}
