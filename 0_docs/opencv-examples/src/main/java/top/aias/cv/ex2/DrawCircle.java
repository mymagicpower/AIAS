package top.aias.cv.ex2;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
 * 绘制圆
 */
public class DrawCircle {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 创建一张空白图像
        Mat image = new Mat(400, 400, CvType.CV_8UC3, new Scalar(255, 255, 255)); // 白色背景

        // 设置圆的颜色（绿色）
        Scalar color = new Scalar(0, 255, 0); // BGR格式：绿色
        int thickness = 2; // 圆的边框粗细

        // 圆心坐标和半径
        Point center = new Point(200, 200);
        int radius = 100;

        // 绘制圆形
        Imgproc.circle(image, center, radius, color, thickness);

        // 显示图像
        HighGui.imshow("Circle Image", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
