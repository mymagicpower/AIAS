package top.aias.cv.ex6;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class DilationExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像，使用灰度模式
        Mat src = Imgcodecs.imread("src/test/resources/dilation.png", Imgcodecs.IMREAD_GRAYSCALE);

        // 创建结构元素（3x3的矩形）
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8));

        // 创建输出图像
        Mat dst = new Mat();

        // 执行膨胀操作
        Imgproc.dilate(src, dst, kernel);

        // 保存结果图像
        Imgcodecs.imwrite("dilated_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
