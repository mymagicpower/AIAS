package top.aias.cv.ex4;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ResizeExample {
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

        // 创建一个空白的图像，用于存储调整大小后的结果
        Mat resizedImage = new Mat();

        // 调整图像大小
        Size size = new Size(300, 300); // 目标尺寸为 300x300
        Imgproc.resize(img, resizedImage, size, 0, 0, Imgproc.INTER_LINEAR);

        // 显示原图像与调整大小后的图像
        HighGui.imshow("Original Image", img);
        HighGui.imshow("Resized Image", resizedImage);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
