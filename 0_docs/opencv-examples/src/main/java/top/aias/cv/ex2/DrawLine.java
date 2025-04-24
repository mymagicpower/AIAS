package top.aias.cv.ex2;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 绘制直线
 */
public class DrawLine {
    private static final Logger logger = LoggerFactory.getLogger(DrawLine.class);

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

        // 设置线条颜色（蓝色）和粗细
        Scalar color = new Scalar(255, 0, 0); // BGR格式：蓝色
        int thickness = 2;

        // 线条起点和终点坐标
        Point pt1 = new Point(50, 50);
        Point pt2 = new Point(350, 350);

        // 在图像上绘制直线
        Imgproc.line(image, pt1, pt2, color, thickness);

        // 显示图像
        HighGui.imshow("Draw", image);

        // 等待用户操作关闭窗口
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
