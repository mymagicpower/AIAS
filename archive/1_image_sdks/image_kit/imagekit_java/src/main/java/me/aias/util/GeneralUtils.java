package me.aias.util;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

/** opencv的一些通用方法工具类 */
public class GeneralUtils {

  private static final int BLACK = 0;
  private static final int WHITE = 255;

  // 设置归一化图像的固定大小
  private static final Size dsize = new Size(32, 32);

  /**
   * 作用：输入图像路径，返回mat矩阵
   *
   * @param imgPath 图像路径
   * @return
   */
  public static Mat matFactory(String imgPath) {
    return opencv_imgcodecs.imread(imgPath);
  }

  /**
   * 作用：输入图像Mat矩阵对象，返回图像的宽度
   *
   * @param src Mat矩阵图像
   * @return
   */
  public static int getImgWidth(Mat src) {
    return src.cols();
  }

  /**
   * 作用：输入图像Mat矩阵，返回图像的高度
   *
   * @param src Mat矩阵图像
   * @return
   */
  public static int getImgHeight(Mat src) {
    return src.rows();
  }

  /**
   * 作用：获取图像(y,x)点的像素，我们只针对单通道(灰度图)
   *
   * @param src Mat矩阵图像
   * @param y y坐标轴
   * @param x x坐标轴
   * @return
   */
  public static int getPixel(Mat src, int y, int x) {
    UByteRawIndexer ldIdx = src.createIndexer();
    int result = ldIdx.get(y, x);
    ldIdx.release();
    return result;
  }

  /**
   * 作用：设置图像(y,x)点的像素，我们只针对单通道(灰度图)
   *
   * @param src Mat矩阵图像
   * @param y y坐标轴 row
   * @param x x坐标轴 col
   * @param color 颜色值[0-255]
   */
  public static void setPixel(Mat src, int y, int x, int color) {
    UByteRawIndexer ldIdx = src.createIndexer();
    ldIdx.put(y, x, color);
    ldIdx.release();
  }

  /**
   * 作用：保存图像
   *
   * @param src Mat矩阵图像
   * @param filePath 要保存图像的路径及名字
   * @return
   */
  public static boolean saveImg(Mat src, String filePath) {
    return opencv_imgcodecs.imwrite(filePath, src);
  }

  /**
   * 作用：翻转图像像素
   * 确保白底黑字或者黑底白字
   *
   * @param src
   * @param b true：表示白底黑字 ， false相反
   * @return
   */
  public static Mat turnPixel(Mat src, boolean b) {
    if (src != null) {
      int width = GeneralUtils.getImgWidth(src);
      int height = GeneralUtils.getImgHeight(src);
      int value;
      int black_num = 0;
      int white_num = 0;
      int i, j;
      for (i = 0; i < width; i++) {
        for (j = 0; j < height; j++) {
          value = GeneralUtils.getPixel(src, j, i);
          if (value == GeneralUtils.getWHITE()) {
            white_num++;
          } else if (value == GeneralUtils.getBLACK()) {
            black_num++;
          }
        }
      }

      if (b && black_num > white_num) {
        // 反转
        src = turnPixel(src);
      } else if (!b && white_num > black_num) {
        // 反转
        src = turnPixel(src);
      }
    }
    return src;
  }

  /**
   * 作用：翻转图像像素
   *
   * @param src Mat矩阵图像
   * @return
   */
  public static Mat turnPixel(Mat src) {
    if (src.channels() != 1) {
      throw new RuntimeException("不是单通道图，需要先灰度化");
    }
    int j, i, value;
    int width = getImgWidth(src), height = getImgHeight(src);
    for (j = 0; j < height; j++) {
      for (i = 0; i < width; i++) {
        value = getPixel(src, j, i);
        if (value == 0) {
          setPixel(src, j, i, WHITE);
        } else {
          setPixel(src, j, i, BLACK);
        }
      }
    }
    return src;
  }

  /** 图像腐蚀/膨胀处理 腐蚀和膨胀对处理没有噪声的图像很有利，慎用 */
  public static Mat erodeDilateImg(Mat src) {
    Mat outImage = new Mat();

    // size 越小，腐蚀的单位越小，图片越接近原图
    Mat structImage =
        opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(2, 2));

    /**
     * 图像腐蚀 腐蚀说明： 图像的一部分区域与指定的核进行卷积， 求核的最`小`值并赋值给指定区域。 腐蚀可以理解为图像中`高亮区域`的'领域缩小'。
     * 意思是高亮部分会被不是高亮部分的像素侵蚀掉，使高亮部分越来越少。
     */
    opencv_imgproc.erode(src, outImage, structImage);
//    opencv_imgproc.erode(src, outImage, structImage, new Point(-1, -1), 2);
    src = outImage;

    /**
     * 膨胀 膨胀说明： 图像的一部分区域与指定的核进行卷积， 求核的最`大`值并赋值给指定区域。 膨胀可以理解为图像中`高亮区域`的'领域扩大'。
     * 意思是高亮部分会侵蚀不是高亮的部分，使高亮部分越来越多。
     */
    opencv_imgproc.dilate(src, outImage, structImage);
//    opencv_imgproc.dilate(src, outImage, structImage, new Point(-1, -1), 2);
    src = outImage;

    return src;
  }

  /**
   * canny算法，边缘检测
   *
   * @param src
   * @return
   */
  public static Mat canny(Mat src) {
    Mat mat = src.clone();
    opencv_imgproc.Canny(src, mat, 60, 200);
    return mat;
  }

  public static int getBLACK() {
    return BLACK;
  }

  public static int getWHITE() {
    return WHITE;
  }

  public static Size getDsize() {
    return dsize;
  }
}
