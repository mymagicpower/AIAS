package top.aias.cv.ex4;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class PerspectiveTransformationExample {
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
        
        // 定义源图像中的四个点
        Point[] srcPoints = { new Point(50, 50), new Point(400, 50), new Point(50, 400), new Point(400, 400) };

        // 定义目标图像中的四个点
        Point[] dstPoints = { new Point(100, 100), new Point(350, 80), new Point(60, 350), new Point(380, 350) };

        // 计算透视变换矩阵
        Mat M = Imgproc.getPerspectiveTransform(new MatOfPoint2f(srcPoints), new MatOfPoint2f(dstPoints));
        
        // 执行透视变换
        Mat dst = new Mat();
        Imgproc.warpPerspective(src, dst, M, new Size(src.cols(), src.rows()));
        
        // 保存结果图像
        Imgcodecs.imwrite("perspective_output.jpg", dst);
        HighGui.imshow("Original Image", src);
        HighGui.imshow("warpAffine Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
