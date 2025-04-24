package top.aias.cv.ex8;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import java.util.*;

public class BoundingRectExample {
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
        
        // 创建一张新的图像用于绘制包围矩形
        Mat dst = Mat.zeros(binary.size(), CvType.CV_8UC3);
        
        // 遍历每个轮廓，计算并绘制包围矩形
        for (int i = 0; i < contours.size(); i++) {
            // 计算轮廓的包围矩形
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            
            // 绘制包围矩形
            Imgproc.rectangle(dst, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 0), 2);
        }
        
        // 保存结果图像
        Imgcodecs.imwrite("bounding_rect_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
