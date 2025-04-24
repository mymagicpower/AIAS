package top.aias.cv.ex1;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ImageShow {
    private static final Logger logger = LoggerFactory.getLogger(ImageShow.class);

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) {
        // 读取图像
        Mat img = Imgcodecs.imread("src/test/resources/girl.png");

        // 显示原图像
        HighGui.imshow("girl", img);

        // 拆分通道
        List<Mat> channels = new ArrayList<>();
        // 将图像通道拆分到 List 中
        Core.split(img, channels);

        // 获取 B、G、R 通道
        Mat b = channels.get(0); // B 通道
        Mat g = channels.get(1); // G 通道
        Mat r = channels.get(2); // R 通道

        // 显示各个通道的图像
        HighGui.imshow("lena_B", b);
        HighGui.imshow("lena_G", g);
        HighGui.imshow("lena_R", r);

        // 等待用户关闭窗口
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
