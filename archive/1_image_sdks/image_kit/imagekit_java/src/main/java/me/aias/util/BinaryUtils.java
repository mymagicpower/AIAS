package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 二值化的工具类
 */
public class BinaryUtils {

    /**
     * opencv自带的二值化
     * @param src
     * @return
     */
    public static Mat binaryNative(Mat src){
        Mat dst = src.clone();
        opencv_imgproc.adaptiveThreshold(src, dst, 255, opencv_imgproc.ADAPTIVE_THRESH_MEAN_C, opencv_imgproc.THRESH_BINARY, 25, 10);
        return dst;
    }


    /**
     * 图像二值化 阀值自适应确定
     *
     * @param src
     *            Mat矩阵图像
     * @return
     */
    public static Mat binaryzation(Mat src) {
        if (src.channels() != 1) {
            throw new RuntimeException("不是单通道图，需要先灰度话！！！");
        }
        int threshold = getAdapThreshold(src);
        return binaryzation(src, threshold);
    }


    /**
     * 图像二值化
     *
     * @param src
     *            Mat矩阵图像
     * @param b
     *            [true/false] true：表示白底黑字，false表示黑底白字
     * @return
     */
    public static Mat binaryzation(Mat src, boolean b) {
        if (src.channels() != 1) {
            throw new RuntimeException("不是单通道图，需要先灰度话！！！");
        }
        int threshold = getAdapThreshold(src);
        return binaryzation(src, threshold, b);
    }

    /**
     * 图像二值化
     *
     * @param src
     *            Mat矩阵图像
     * @param threshold
     *            阀值
     * @return
     */
    public static Mat binaryzation(Mat src, int threshold) {
        if (src.channels() != 1) {
            throw new RuntimeException("不是单通道图，需要先灰度化");
        }
        return binaryzation(src, threshold, true);
    }

    /**
     * 图像二值化
     *
     * @param src
     *            Mat矩阵图像
     * @param threshold
     *            阀值
     * @param b
     *            [true/false] true：表示白底黑字，false表示黑底白字
     * @return
     */
    public static Mat binaryzation(Mat src, int threshold, boolean b) {
        if (src.channels() != 1 || threshold < 0) {
            System.out.println("不是单通道图像或者阀值异常");
            return src;
        }
        int nWhite_sum = 0, nBlack_sum = 0;
        int i, j;
        int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
        int value;
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                value = GeneralUtils.getPixel(src, j, i);
                if (value > threshold) {
                    GeneralUtils.setPixel(src, j, i, GeneralUtils.getWHITE());
                    nWhite_sum++;
                } else {
                    GeneralUtils.setPixel(src, j, i, GeneralUtils.getBLACK());
                    nBlack_sum++;
                }
            }
        }

        if (b) {
            // 白底黑字
            if (nBlack_sum > nWhite_sum) {
                src = GeneralUtils.turnPixel(src);
            }
        } else {
            // 黑底白字
            if (nWhite_sum > nBlack_sum) {
                src = GeneralUtils.turnPixel(src);
            }
        }
        return src;
    }

    /**
     * 作用：自适应选取阀值
     *
     * @param src
     *            Mat矩阵图像
     * @return
     */
    public static int getAdapThreshold(Mat src) {
        int threshold = 0, threshold_new = 127;
        int nWhite_count, nBlack_count;
        int nWhite_sum, nBlack_sum;
        int value, i, j;
        int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

        if(width == 0 || height == 0){
            System.out.println("图像加载异常");
            return -1;
        }

        while (threshold != threshold_new) {
            nWhite_sum = nBlack_sum = 0;
            nWhite_count = nBlack_count = 0;
            for (j = 0; j < height; j++) {
                for (i = 0; i < width; i++) {
                    value = GeneralUtils.getPixel(src, j, i);
                    if (value > threshold_new) {
                        nWhite_count++;
                        nWhite_sum += value;
                    } else {
                        nBlack_count++;
                        nBlack_sum += value;
                    }
                }
            }
            threshold = threshold_new;
            if(nWhite_count == 0 || nBlack_count == 0){
                threshold_new = (nWhite_sum + nBlack_sum) / (nWhite_count + nBlack_count);
            }else{
                threshold_new = (nWhite_sum / nWhite_count + nBlack_sum / nBlack_count) / 2;
            }

        }

        return threshold;
    }




    /**
     * 局部自适应二值化
     * @param src
     * @return
     */
    public static Mat partBinaryzation(Mat src){
        int width = GeneralUtils.getImgWidth(src);
        int height = GeneralUtils.getImgHeight(src);
        int value;
        for(int i = 0 ; i < width ; i++){
            Mat partMat = src.col(i);
            int thresold = getAdapThreshold(partMat);
            int black_num = 0 , white_num = 0;


            for(int j = 0 ; j < height ; j++){
                value = GeneralUtils.getPixel(src , j , i);
                if(value > thresold){
                    GeneralUtils.setPixel(src , j , i , GeneralUtils.getWHITE());
                    white_num++;
                }else{
                    GeneralUtils.setPixel(src, j, i, GeneralUtils.getBLACK());
                    black_num++;
                }
            }
        }

        return src;
    }

}