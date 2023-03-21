package me.aias;

import me.aias.util.GeneralUtils;
import me.aias.util.GrayUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.Test;


/**
 * 测试灰度化
 * Testing grayscale
 */
public class TestGrayUtils {
    @Test
    /**
     * 测试opencv自带的灰度化方法
     * Testing OpenCV's built-in grayscale method
     */
    public void testGrayNative(){
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayNative(src);

        GeneralUtils.saveImg(src , destPath + "grayNative.png");
    }

    @Test
    /**
     * 测试细粒度灰度化方法
     * Testing fine-grained grayscale method
     * 均值灰度化减噪
     * Mean grayscale denoising
     */
    public void testGrayColByMidle() {
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByMidle(src);

        GeneralUtils.saveImg(src , destPath + "grayRowByMidle.png");
    }


    @Test
    /**
     * 测试细粒度灰度化方法
     * Testing fine-grained grayscale method
     * k值灰度化减噪
     * K-value grayscale denoising
     */
    public void testGrayColByKLargest() {
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByKLargest(src);

        GeneralUtils.saveImg(src , destPath + "grayRowByKLargest.png");
    }

    @Test
    /**
     * 测试细粒度灰度化方法
     * Testing fine-grained grayscale method
     * 局部自适应阀值灰度化减噪
     * Local adaptive threshold grayscale denoising
     */
    public void testGrayColByPartAdapThreshold() {
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByPartAdapThreshold(src);

        GeneralUtils.saveImg(src , destPath + "grayColByPartAdapThreshold.png");
    }


    @Test
    /**
     * 测试细粒度灰度化方法
     * Testing fine-grained grayscale method
     * 全局自适应阀值灰度化减噪
     * Global adaptive threshold grayscale denoising
     */
    public void testGrayColByAdapThreshold() {
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByAdapThreshold(src);

        GeneralUtils.saveImg(src , destPath + "grayColByAdapThreshold.png");
    }


}