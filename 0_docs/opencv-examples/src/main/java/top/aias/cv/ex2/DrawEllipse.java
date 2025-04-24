package top.aias.cv.ex2;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
 * 绘制椭圆
 */
public class DrawEllipse {
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

        // 设置椭圆的颜色（蓝色）
        Scalar color = new Scalar(255, 0, 0); // BGR格式：蓝色
        int thickness = 2; // 椭圆边框粗细

        // 椭圆的中心点、长短轴和旋转角度
        Point center = new Point(200, 200);
        Size axes = new Size(150, 100); // 水平半轴为150，垂直半轴为100
        double angle = 45; // 旋转角度，逆时针方向
        double startAngle = 0; // 起始角度
        double endAngle = 360; // 结束角度

        // 绘制椭圆
        Imgproc.ellipse(image, center, axes, angle, startAngle, endAngle, color, thickness);

        // 显示图像
        HighGui.imshow("Ellipse Image", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
