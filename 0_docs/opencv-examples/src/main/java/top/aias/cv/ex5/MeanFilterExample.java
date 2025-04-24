package top.aias.cv.ex5;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class MeanFilterExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像
        Mat src = Imgcodecs.imread("src/test/resources/noise.jpg");
        
        // 创建输出图像
        Mat dst = new Mat();
        
        // 使用 3x3 的均值滤波器进行滤波
        Imgproc.blur(src, dst, new Size(3, 3));
        
        // 保存结果图像
        Imgcodecs.imwrite("output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
