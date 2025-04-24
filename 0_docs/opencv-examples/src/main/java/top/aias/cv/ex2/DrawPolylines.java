package top.aias.cv.ex2;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制多边形
 */
public class DrawPolylines {
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

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(200, 200));
        points.add(new Point(250, 250));
        points.add(new Point(300, 200));

        // 将点转换为 MatOfPoint 对象
        MatOfPoint polyline = new MatOfPoint();
        polyline.fromList(points);

        // 将 MatOfPoint 对象添加到 List 中
        List<MatOfPoint> polylines = new ArrayList<MatOfPoint>();
        polylines.add(polyline);

        // 设置折线颜色（绿色）和粗细
        Scalar color = new Scalar(0, 255, 0); // BGR格式：绿色
        int thickness = 2; // 折线粗细

        // 绘制多条折线
        Imgproc.polylines(image, polylines, true, color, thickness);

        // 显示图像
        HighGui.imshow("Polylines Image", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
