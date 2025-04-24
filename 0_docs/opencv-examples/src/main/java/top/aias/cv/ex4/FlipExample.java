package top.aias.cv.ex4;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class FlipExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像文件
        Mat img = Imgcodecs.imread("src/test/resources/girl.png");

        // 创建一个空白的图像，用于存储翻转后的结果
        Mat flippedImage = new Mat();

        // 翻转图像，沿水平轴翻转（上下翻转）
        Core.flip(img, flippedImage, 0);

        // 显示原图像与翻转后的图像
        HighGui.imshow("Original Image", img);
        HighGui.imshow("Flipped Image", flippedImage);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
