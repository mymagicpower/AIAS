package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.util.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 人脸对齐
 * Face Align
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */

public class FaceAlignExample {
  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    process("src/test/resources/beauty.jpg");
  }

  public static void process(String facePath)
      throws IOException, ModelException, TranslateException {
    Path path = Paths.get(facePath);
    ImageFactory defFactory = new OpenCVImageFactory();
    Image image = defFactory.fromFile(path);

    // topk值 - topk value
    int topK = 500;
    // 置信度阈值
    // Confidence threshold
    double confThresh = 0.85f;
    // 非极大值抑制阈值
    // Non-maximum suppression threshold
    double nmsThresh = 0.45f;
    LightFaceDetection lightFaceDetection = new LightFaceDetection();
    try (ZooModel<Image, DetectedObjects> model =
            ModelZoo.loadModel(lightFaceDetection.criteria(topK, confThresh, nmsThresh));
        Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
      DetectedObjects detections = predictor.predict(image);

      // 保存检测人脸
      // Save the detected faces
      ImageUtils.saveBoundingBoxImage(image, detections, "faces_detected.png", "build/output");

      List<DetectedObjects.DetectedObject> list = detections.items();

      int index = 0;
      for (DetectedObjects.DetectedObject result : list) {
        BoundingBox box = result.getBoundingBox();
        Rectangle rectangle = box.getBounds();
        // 人脸抠图
        // Crop the face image
        // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
        // factor = 0.1f, meaning to expand by 10%, to prevent the face from being partially cut off after affine transformation
        Rectangle subImageRect =
            FaceUtils.getSubImageRect(rectangle, image.getWidth(), image.getHeight(), 1.0f);
        int x = (int) (subImageRect.getX());
        int y = (int) (subImageRect.getY());
        int w = (int) (subImageRect.getWidth());
        int h = (int) (subImageRect.getHeight());
        Image subImage = image.getSubImage(x, y, w, h);

        // 保存，抠出的人脸图
        // Save the cropped face image
        ImageUtils.saveImage(subImage, "face_" + index + ".png", "build/output");

        // 获取人脸关键点列表
        // Get the list of face keypoints
        List<Point> points = (List<Point>) box.getPath();
        //      人脸关键点坐标对应的人脸部位
        //      Face parts corresponding to the coordinates of the facial keypoints
        //      1.  left_eye_x , left_eye_y
        //      2.  right_eye_x , right_eye_y
        //      3.  nose_x , nose_y
        //      4.  left_mouth_x , left_mouth_y
        //      5.  right_mouth_x , right_mouth_y
        // 计算人脸关键点在子图中的新坐标
        // Calculate the new coordinates of facial keypoints in the sub-image
        double[][] pointsArray = FaceUtils.pointsArray(subImageRect, points);

        // 转 NDArray - Convert to NDArray
        NDManager manager = NDManager.newBaseManager();
        NDArray srcPoints = manager.create(pointsArray);
        NDArray dstPoints = SVDUtils.point112x112(manager);

        // 定制的5点仿射变换 - Custom 5-point affine transformation
        Mat svdMat = NDArrayUtils.toOpenCVMat(manager, srcPoints, dstPoints);
        Mat mat = FaceAlignment.get5WarpAffineImg((Mat)subImage.getWrappedImage(), svdMat);

        int width = mat.width() > 112 ? 112 : mat.width();
        int height = mat.height() > 112 ? 112 : mat.height();
        Image img = OpenCVImageFactory.getInstance().fromImage(mat).getSubImage(0, 0, width, height);
        ImageUtils.saveImage(img, "face_align_" + index++ + ".png", "build/output");
      }
    }
  }
}
