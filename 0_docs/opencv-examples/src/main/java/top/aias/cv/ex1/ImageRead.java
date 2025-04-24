package top.aias.cv.ex1;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageRead {
    private static final Logger logger = LoggerFactory.getLogger(ImageRead.class);

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws IOException {

        Path imageFile = Paths.get("src/test/resources/t1.png");

        // 读取图像
        Mat img = Imgcodecs.imread(imageFile.toString());

        // 检查图像是否加载成功
        if (img.empty()) {
            System.out.println("图像加载失败！");
            return;
        }

        // 输出数据类型
        System.out.println("数据类型: " + img.getClass().getName());

        // 输出图像形状
        System.out.println("图像形状: " + img.rows() + "x" + img.cols() + "x" + img.channels());

        // 输出元素数据类型
        System.out.println("元素数据类型: " + CvType.typeToString(img.type()));

        // 输出图像数组的元素总数
        System.out.println("数组元素的个数: " + img.total() * img.channels());
    }
}
