package top.aias.cv.ex2;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
 * 绘制矩形
 */
public class DrawRectangle {

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

        // 设置矩形颜色（红色）
        // BGR格式：红色
        Scalar color = new Scalar(0, 0, 255);
        int thickness = 2; // 矩形边框粗细

        // 矩形的左上角和右下角坐标
        Point pt1 = new Point(50, 50);
        Point pt2 = new Point(350, 350);

        // 绘制矩形
        Imgproc.rectangle(image, pt1, pt2, color, thickness);

        // 显示图像
        HighGui.imshow("Rectangle Image", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
