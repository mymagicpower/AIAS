package top.aias.cv.ex7;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class CannyEdgeDetectionExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像，使用灰度模式
        Mat src = Imgcodecs.imread("src/test/resources/bird.png", Imgcodecs.IMREAD_GRAYSCALE);

        // 创建输出图像
        Mat edges = new Mat();

        // 执行 Canny 边缘检测
        // 第一个参数：输入图像
        // 第二个参数：低阈值
        // 第三个参数：高阈值
        Imgproc.Canny(src, edges, 100, 200);

        // 保存边缘检测结果图像
        Imgcodecs.imwrite("edges_output.jpg", edges);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", edges);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
