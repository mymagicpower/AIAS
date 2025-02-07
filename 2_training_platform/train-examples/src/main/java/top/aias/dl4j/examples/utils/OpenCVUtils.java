package top.aias.dl4j.examples.utils;

import org.bytedeco.opencv.opencv_core.Mat;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import top.aias.dl4j.examples.detection.models.BaseModel;

import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.FONT_HERSHEY_DUPLEX;

/**
 * 绘图工具类
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class OpenCVUtils {
    public static void putText(org.opencv.core.Mat image, String text, Point org) {
        // 设置文本字符串、字体、颜色和位置
        // Point org = new Point(10, 25);  // 文本起始点
        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX; // 2
        double fontScale = 0.8;          // 字体大小
        Scalar color = new Scalar(0, 255, 0); // 绿色文本
        int thickness = 1;               // 文本粗细

        // 在图像上添加文本
        Imgproc.putText(image, text, org, fontFace, fontScale, color, thickness, Imgproc.LINE_AA);

    }

    public static org.opencv.core.Mat convert(Mat javaCVMat) {
        // 获取 JavaCV Mat 的大小和类型
        int rows = javaCVMat.rows();
        int cols = javaCVMat.cols();
        int channels = javaCVMat.channels();

        // 创建一个 OpenCV Mat 对象，确保类型与 JavaCV Mat 一致
        org.opencv.core.Mat openCVMat = new org.opencv.core.Mat(rows, cols, CvType.CV_8UC3); // 假设是 8 位 3 通道的图像

        // 获取 JavaCV Mat 数据（byte 数组）
        byte[] data = new byte[rows * cols * channels];
        javaCVMat.data().get(data); // 使用 data() 获取数据
        // 将数据填充到 OpenCV Mat 中
        openCVMat.put(0, 0, data);

        return openCVMat;
    }

    public static void drawBoxes(BaseModel model,  List<DetectedObject> objects, org.opencv.core.Mat image) {
        for (DetectedObject obj : objects) {
            double[] xy1 = obj.getTopLeftXY();
            double[] xy2 = obj.getBottomRightXY();
            int predictedClass = obj.getPredictedClass();
            String label = model.getLabels().get(predictedClass);
            double prob = obj.getConfidence();
            // 使用 String.format() 进行四舍五入并保留2位小数
            String formattedProb = String.format("%.2f", prob);

            int x1 = (int) Math.round(model.getWidth() * xy1[0] / model.getGridWidth());
            int y1 = (int) Math.round(model.getHeight() * xy1[1] / model.getGridHeight());
            int x2 = (int) Math.round(model.getWidth() * xy2[0] / model.getGridWidth());
            int y2 = (int) Math.round(model.getHeight() * xy2[1] / model.getGridHeight());
            org.opencv.core.Scalar green = new org.opencv.core.Scalar(0, 255, 0); // 绿色文本
            Imgproc.rectangle(image, new org.opencv.core.Point(x1, y1), new org.opencv.core.Point(x2, y2), green);
            Imgproc.putText(image, label+ " - " + formattedProb, new org.opencv.core.Point(x1 + 2, y2 - 2), FONT_HERSHEY_DUPLEX, 0.8, green);
        }
    }
}
