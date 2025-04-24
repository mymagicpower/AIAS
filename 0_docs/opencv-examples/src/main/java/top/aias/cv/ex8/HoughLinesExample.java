package top.aias.cv.ex8;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class HoughLinesExample {
    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {

        // 读取图像并转换为灰度图
        Mat src = Imgcodecs.imread("src/test/resources/hf.png", Imgcodecs.IMREAD_GRAYSCALE);
        
        // 使用 Canny 边缘检测
        Mat edges = new Mat();
        Imgproc.Canny(src, edges, 50, 150);
        
        // 存储检测到的直线
        Mat lines = new Mat();
        
        // 霍夫变换检测直线
        Imgproc.HoughLines(edges, lines, 1, Math.PI / 180, 150);
        
        // 创建一张空白图像用于绘制直线
        Mat dst = Mat.zeros(src.size(), CvType.CV_8UC3);
        
        // 遍历所有检测到的直线
        for (int i = 0; i < lines.rows(); i++) {
            double[] data = lines.get(i, 0);
            double rho = data[0];
            double theta = data[1];
            
            // 计算直线的两个端点
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            double x0 = rho * cosTheta;
            double y0 = rho * sinTheta;
            double x1 = Math.round(x0 + 1000 * (-sinTheta));
            double y1 = Math.round(y0 + 1000 * (cosTheta));
            double x2 = Math.round(x0 - 1000 * (-sinTheta));
            double y2 = Math.round(y0 - 1000 * (cosTheta));
            
            // 绘制直线
            Imgproc.line(dst, new Point(x1, y1), new Point(x2, y2), new Scalar(0, 0, 255), 2);
        }
        
        // 保存结果图像
        Imgcodecs.imwrite("hough_lines_output.jpg", dst);
        HighGui.imshow("before Image", src);
        HighGui.imshow("after Image", dst);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
