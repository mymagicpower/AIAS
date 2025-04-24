package top.aias.cv.ex2;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
 * 绘制文本
 */
public class PutText {
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

        // 设置文本字符串、字体、颜色和位置
        String text = "Hello, OpenCV!";
        Point org = new Point(50, 200);  // 文本起始点
        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
        double fontScale = 1.0;          // 字体大小
        Scalar color = new Scalar(0, 0, 255); // 红色文本
        int thickness = 2;               // 文本粗细

        // 在图像上添加文本
        Imgproc.putText(image, text, org, fontFace, fontScale, color, thickness, Imgproc.LINE_AA);

        // 显示图像
        HighGui.imshow("Text on Image", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
