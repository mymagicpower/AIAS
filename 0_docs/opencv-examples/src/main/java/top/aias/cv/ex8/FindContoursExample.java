package top.aias.cv.ex8;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

import java.util.ArrayList;

public class FindContoursExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像，使用灰度模式
        Mat src = Imgcodecs.imread("src/test/resources/contour.png", Imgcodecs.IMREAD_GRAYSCALE);

        // 进行阈值化操作，将图像转换为二值图像
        Mat thresh = new Mat();
        Imgproc.threshold(src, thresh, 128, 255, Imgproc.THRESH_BINARY);

        // 存储轮廓的容器
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        // 存储轮廓的层次结构（可选）
        Mat hierarchy = new Mat();

        // 查找轮廓
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 在原图上绘制轮廓
        Mat dst = Mat.zeros(src.size(), CvType.CV_8UC3); // 创建一个空白的彩色图像
        Imgproc.drawContours(dst, contours, -1, new Scalar(0, 255, 0), 2);

        // 保存结果图像
        Imgcodecs.imwrite("contours_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
