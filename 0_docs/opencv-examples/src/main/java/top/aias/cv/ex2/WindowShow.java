package top.aias.cv.ex2;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建和关闭窗口
 */
public class WindowShow {
    private static final Logger logger = LoggerFactory.getLogger(WindowShow.class);

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

        // 等待用户关闭窗口
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
//        HighGui.destroyWindow( "girl");
    }
}
