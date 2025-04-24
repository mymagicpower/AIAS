package top.aias.cv.ex7;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class SobelEdgeDetectionExample {
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
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat grad = new Mat();

        // 使用 Sobel 算子计算梯度
        // 第一个参数：输入图像
        // 第二个参数：输出图像
        // 第三个参数：ddepth：输出图像的深度，通常为 -1，表示与输入图像相同
        // 第四个参数：x 或 y 方向的 Sobel 核大小（3x3）
        // 第五个参数和第六个参数：Sobel 算子的 X 和 Y 方向的阶数
        // 此处为计算 X 和 Y 方向的梯度
        Imgproc.Sobel(src, grad_x, CvType.CV_16S, 1, 0, 3);
        Imgproc.Sobel(src, grad_y, CvType.CV_16S, 0, 1, 3);

        // 计算梯度幅度
        Core.addWeighted(grad_x, 1, grad_y, 1, 0, grad);

        // 将图像转换回 CV_8U 类型（无符号 8 位整数）
        Mat abs_grad = new Mat();
        Core.convertScaleAbs(grad, abs_grad);

        // 保存结果图像
        Imgcodecs.imwrite("sobel_output.jpg", abs_grad);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", abs_grad);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
