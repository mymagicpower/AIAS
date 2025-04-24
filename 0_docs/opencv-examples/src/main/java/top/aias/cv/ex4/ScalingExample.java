package top.aias.cv.ex4;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class ScalingExample {
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
        
        // 计算缩放矩阵，缩放因子为 2.0（放大 2 倍）
        Mat M = Mat.zeros(2, 3, CvType.CV_64F);
        M.put(0, 0, 2.0);  // X 轴缩放因子
        M.put(1, 1, 2.0);  // Y 轴缩放因子
        
        // 执行仿射变换
        Mat dst = new Mat();
        Imgproc.warpAffine(src, dst, M, new Size(src.cols() * 2, src.rows() * 2));
        
        // 保存结果图像
        Imgcodecs.imwrite("scaled_output.jpg", dst);
        HighGui.imshow("Original Image", src);
        HighGui.imshow("warpAffine Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
