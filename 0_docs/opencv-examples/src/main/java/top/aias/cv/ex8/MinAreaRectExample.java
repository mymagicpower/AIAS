package top.aias.cv.ex8;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import java.util.*;

public class MinAreaRectExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像并转为灰度图
        Mat src = Imgcodecs.imread("src/test/resources/contour.png", Imgcodecs.IMREAD_GRAYSCALE);

        // 二值化图像
        Mat binary = new Mat();
        Imgproc.threshold(src, binary, 128, 255, Imgproc.THRESH_BINARY);
        
        // 存储轮廓信息
        List<MatOfPoint> contours = new ArrayList<>();
        
        // 存储轮廓的层级信息
        Mat hierarchy = new Mat();
        
        // 查找轮廓
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        // 创建一张新的图像用于绘制最小外接矩形
        Mat dst = Mat.zeros(binary.size(), CvType.CV_8UC3);
        
        // 遍历每个轮廓，计算并绘制最小外接矩形
        for (int i = 0; i < contours.size(); i++) {
            // 计算最小外接矩形
            RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            
            // 获取矩形的角点
            Point[] boxPoints = new Point[4];
            rotatedRect.points(boxPoints);
            
            // 绘制最小外接矩形
            for (int j = 0; j < 4; j++) {
                Imgproc.line(dst, boxPoints[j], boxPoints[(j + 1) % 4], new Scalar(0, 255, 0), 2);
            }
        }
        
        // 保存结果图像
        Imgcodecs.imwrite("min_area_rect_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
