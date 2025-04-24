package top.aias.cv.ex4;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class ThreePointMappingExample {
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
        
        // 源图像中的三点
        Point[] srcPoints = { new Point(50, 50), new Point(200, 50), new Point(50, 200) };

        // 目标图像中的三点
        Point[] dstPoints = {new Point(18, 68),  new Point(148, 143), new Point(-57, 93) };
        // 计算仿射变换矩阵
        Mat M = Imgproc.getAffineTransform(new MatOfPoint2f(srcPoints), new MatOfPoint2f(dstPoints));
        
        // 执行仿射变换
        Mat dst = new Mat();
        Imgproc.warpAffine(src, dst, M, new Size(src.cols(), src.rows()));
        
        // 保存结果图像
        Imgcodecs.imwrite("output.jpg", dst);
        HighGui.imshow("Original Image", src);
        HighGui.imshow("warpAffine Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
