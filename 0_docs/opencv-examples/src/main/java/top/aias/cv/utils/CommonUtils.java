package top.aias.cv.utils;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 读图片工具类
 */
public class CommonUtils {
    /**
     * 读取图片
     *
     * @param imgPath 图像路径
     * @return
     */
    public static Mat read(String imgPath) {

        return Imgcodecs.imread(imgPath);
    }

    /**
     * 读取图片，将图像转换为单通道灰度图像
     *
     * @param imgPath 图像路径
     * @return
     */
    public static Mat readGrayScale(String imgPath) {

        return Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_GRAYSCALE);
    }

    /**
     * 保存图像
     *
     * @param src Mat矩阵图像
     * @param filePath 要保存图像的路径及名字
     * @return
     */
    public static boolean saveImg(Mat src, String filePath) {

        return Imgcodecs.imwrite(filePath, src);
    }

    /**
     * 灰度化
     * @param src
     * @return
     */
    public static Mat grayNative(Mat src){
        Mat gray = src.clone();
        if(src.channels() == 3){
            //进行灰度化
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
            src = gray;
        }
        return src;
    }
}
