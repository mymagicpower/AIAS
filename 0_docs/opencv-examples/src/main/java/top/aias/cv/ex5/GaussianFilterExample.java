package top.aias.cv.ex5;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class GaussianFilterExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像
        Mat src = Imgcodecs.imread("src/test/resources/gaussian_noise.jpg");
        
        // 创建输出图像
        Mat dst = new Mat();
        
        // 使用 5x5 的高斯滤波器，标准差为 1.5
        Imgproc.GaussianBlur(src, dst, new Size(5, 5), 1.5);
        
        // 保存结果图像
        Imgcodecs.imwrite("output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
