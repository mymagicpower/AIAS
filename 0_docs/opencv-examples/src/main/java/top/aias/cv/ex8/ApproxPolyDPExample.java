package top.aias.cv.ex8;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import java.util.*;

public class ApproxPolyDPExample {
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
        
        // 创建一张新的图像用于绘制近似轮廓
        Mat dst = Mat.zeros(binary.size(), CvType.CV_8UC3);
        
        // 定义近似精度（较大的值会导致更简化的多边形）
        double epsilon = 0.04 * Imgproc.arcLength(new MatOfPoint2f(contours.get(0).toArray()), true);  // 计算轮廓的周长
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        
        // 对每个轮廓进行多边形近似
        for (int i = 0; i < contours.size(); i++) {
            // 将轮廓从 MatOfPoint 转换为 MatOfPoint2f
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            
            // 进行多边形近似
            Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);
            
            // 将 MatOfPoint2f 转换为 MatOfPoint
            MatOfPoint approxPoints = new MatOfPoint(approxCurve.toArray());
            
            // 绘制近似的轮廓
            Imgproc.drawContours(dst, Arrays.asList(approxPoints), -1, new Scalar(0, 255, 0), 2);
        }
        
        // 保存结果图像
        Imgcodecs.imwrite("approx_poly_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
