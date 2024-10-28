package me.aias;

import me.aias.util.BinaryUtils;
import me.aias.util.GeneralUtils;
import me.aias.util.GrayUtils;
import me.aias.util.NoiseUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.Test;

/**
 * 测试降噪
 * Test denoising
 */
public class TestRemoveNoiseUtils {

    @Test
    /**
     * 测试8邻域降噪
     * Test 8-neighborhood denoising
     */
    public void testNativeRemoveNoise(){
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/noise/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByPartAdapThreshold(src);

        src = BinaryUtils.binaryzation(src);

        // 8邻域降噪
        // 8-neighborhood denoising
        src = NoiseUtils.navieRemoveNoise(src , 1);

        GeneralUtils.saveImg(src , destPath + "nativeRemoveNoise.png");

    }

    @Test
    /**
     * 连通域降噪
     * Connected domain denoising
     */
    public void testConnectedRemoveNoise(){
        String imgPath = "src/test/resources/1.png";
        String destPath = "build/output/noise/";

        Mat src = GeneralUtils.matFactory(imgPath);

        src = GrayUtils.grayColByPartAdapThreshold(src);

        src = BinaryUtils.binaryzation(src);

        // 连通域降噪 - Connected domain denoising
        src = NoiseUtils.connectedRemoveNoise(src , 1);

        GeneralUtils.saveImg(src , destPath + "connectedRemoveNoise.png");

    }
}