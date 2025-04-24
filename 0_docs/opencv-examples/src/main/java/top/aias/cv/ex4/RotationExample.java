package top.aias.cv.ex4;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class RotationExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像
        Mat src = Imgcodecs.imread("src/test/resources/girl.png");
        
        // 获取图像的中心点
        Point center = new Point(src.cols() / 2, src.rows() / 2);
        
        // 计算旋转矩阵，旋转 45 度，缩放因子为 1.0
        Mat M = Imgproc.getRotationMatrix2D(center, 45, 1.0);
        
        // 执行仿射变换
        Mat dst = new Mat();
        Imgproc.warpAffine(src, dst, M, new Size(src.cols(), src.rows()));

        // 保存结果图像
        Imgcodecs.imwrite("rotated_output.jpg", dst);

        // 显示原图像与翻转后的图像
        HighGui.imshow("Original Image", src);
        HighGui.imshow("warpAffine Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
