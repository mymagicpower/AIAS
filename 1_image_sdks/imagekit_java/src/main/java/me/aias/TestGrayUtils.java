package me.aias;

import me.aias.util.GeneralUtils;
import me.aias.util.GrayUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.Test;


/**
 * 测试灰度化
 */
public class TestGrayUtils {
    @Test
    /**
     * 测试opencv自带的灰度化方法
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
     * 均值灰度化减噪
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
     * k值灰度化减噪
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
     * 局部自适应阀值灰度化减噪
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
     * 全局自适应阀值灰度化减噪
     */
    public void testGrayColByAdapThreshold() {
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/gray/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByAdapThreshold(src);

        GeneralUtils.saveImg(src , destPath + "grayColByAdapThreshold.png");
    }


}