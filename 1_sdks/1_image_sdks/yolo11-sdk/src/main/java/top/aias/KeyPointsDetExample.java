package top.aias;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.Joints;
import ai.djl.opencv.OpenCVImageFactory;
import ai.onnxruntime.OrtException;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import top.aias.beans.KeyPointsDetResult;
import top.aias.models.Yolo11DetKeyPointsModel;
import top.aias.utils.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 关键点检测，COCO数据集
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Slf4j
public class KeyPointsDetExample {

    public static void main(String[] args) throws OrtException, IOException {
        String modelPath = "src/main/resources/yolo11x-pose.onnx";
        Path path = Paths.get("src/main/resources/img.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(path);

        Yolo11DetKeyPointsModel model = new Yolo11DetKeyPointsModel(modelPath);
        KeyPointsDetResult result = model.call(image);
        draw(image, result);

    }

    private static void draw(Image image, KeyPointsDetResult result) throws IOException {
        image.drawBoundingBoxes(result.getDetectedObjects(), 0.8f);
        Mat mat = (Mat) image.getWrappedImage();

        List<Joints> jointsList = result.getJointsList();
        for (Joints joints : jointsList) {
            List<Joints.Joint> list = joints.getJoints();
            for (Joints.Joint joint : list) {
                if (joint.getConfidence() >= 0.7) {
                    Imgproc.circle(mat, new Point((int) joint.getX(), (int) joint.getY()), 5, OpenCVUtils.getColor(), -1, Imgproc.LINE_AA, 0);
                }
            }

            drawSkeleton(mat, list);

        }

        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        Path imagePath = outputDir.resolve("kp_result.png");
        image.save(Files.newOutputStream(imagePath), "png");
    }

    // 绘制骨架连接线
    public static void drawSkeleton(Mat mat, List<Joints.Joint> joints) {
        int[][] skeleton = {
                {0, 1}, {0, 2}, {1, 3}, {2, 4},   // 头部和脸部
                {5, 6}, {5, 7}, {6, 8},           // 肩膀和手臂上段
                {7, 9}, {8, 10},                  // 手臂下段
                {5, 11}, {6, 12},                 // 肩膀到臀部
                {11, 12},                         // 腰部连接
                {11, 13}, {12, 14},               // 腰到膝盖
                {13, 15}, {14, 16}                // 膝盖到脚踝
        };

        // 遍历每对关键点，绘制连线
        for (int[] points : skeleton) {
            Joints.Joint pt1 = joints.get(points[0]);
            Joints.Joint pt2 = joints.get(points[1]);

            if (pt1.getConfidence() >= 0.7 && pt2.getConfidence() >= 0.7) {
                Imgproc.line(mat,
                        new Point((int) pt1.getX(), (int) pt1.getY()),
                        new Point((int) pt2.getX(), (int) pt2.getY()),
                        new Scalar(0, 255, 0, 1), 2, Imgproc.LINE_AA, 0
                );
            }
        }
    }
}