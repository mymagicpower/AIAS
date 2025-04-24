package top.aias.cv.ex4;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class TranslationExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像文件
        Mat src = Imgcodecs.imread("src/test/resources/girl.png");
        
        // 定义平移矩阵 [1, 0, tx], [0, 1, ty]
        Mat M = Mat.zeros(2, 3, CvType.CV_64F);
        M.put(0, 0, 1);
        M.put(0, 2, 50);  // 沿 X 轴平移 50 像素
        M.put(1, 1, 1);
        M.put(1, 2, 30);  // 沿 Y 轴平移 30 像素
        
        // 执行仿射变换
        Mat dst = new Mat();
        Imgproc.warpAffine(src, dst, M, new Size(src.cols(), src.rows()));

        // 保存结果图像
        Imgcodecs.imwrite("translated_output.jpg", dst);

        // 显示原图像与翻转后的图像
        HighGui.imshow("Original Image", src);
        HighGui.imshow("warpAffine Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
