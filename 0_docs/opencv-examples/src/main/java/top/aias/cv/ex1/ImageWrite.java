package top.aias.cv.ex1;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ImageWrite {
    private static final Logger logger = LoggerFactory.getLogger(ImageWrite.class);

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws IOException {

        // 创建大小为 50x50 的黑色正方形图像
        Mat img = new Mat(50, 50, CvType.CV_8UC1); // 单通道灰度图像
        img.setTo(new org.opencv.core.Scalar(0)); // 填充为黑色

        // 将图像写入文件
        boolean success = Imgcodecs.imwrite("output/ImageWrite.jpg", img);

        // 检查是否写入成功
        if (success) {
            System.out.println("图像成功保存为 ImageWrite.jpg");
        } else {
            System.out.println("图像保存失败！");
        }
    }
}
