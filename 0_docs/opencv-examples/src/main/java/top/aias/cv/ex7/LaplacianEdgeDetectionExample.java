package top.aias.cv.ex7;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class LaplacianEdgeDetectionExample {
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
        Mat dst = new Mat();

        // 应用 Laplacian 操作
        // 参数说明：
        // 1. 输入图像（灰度图像）
        // 2. 输出图像
        // 3. ddepth：输出图像深度，-1 表示与输入图像相同
        // 4. ksize：Sobel 核大小，推荐为 3 或 5
        Imgproc.Laplacian(src, dst, CvType.CV_16S, 3);

        // 将图像转换回 CV_8U 类型（无符号 8 位整数）
        Mat abs_dst = new Mat();
        Core.convertScaleAbs(dst, abs_dst);

        // 保存结果图像
        Imgcodecs.imwrite("laplacian_output.jpg", abs_dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", abs_dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
