package me.aias.util;

import org.bytedeco.opencv.opencv_core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像处理工具类1.0.0(只针对几乎没有畸变的图像) 灰度话、二值化、降噪、切割、归一化
 * Image processing utility class 1.0.0 (only for images with almost no distortion) Grayscale, binarization, denoising, cutting, normalization
 *
 * @author admin
 */
public class TableUtils {
  private static final int BLACK = 0;

  private TableUtils() {};

  /**
   * 统计图像每行/每列黑色像素点的个数
   * Count the number of black pixels per row/column in the image
   * (n1,n2)=>(height,width),
   * b=true; 统计每行 - count per row
   * (n1,n2)=>(width,height),
   * b=false; 统计每列 - count per column
   *
   * @param src Mat矩阵对象 - Mat object
   * @param n1
   * @param n2
   * @param b true表示统计每行;false表示统计每列 - true to count each row; false to count each column
   * @return
   */
  public static int[] countPixel(Mat src, int n1, int n2, boolean b) {
    int[] xNum = new int[n1];
    for (int i = 0; i < n1; i++) {
      for (int j = 0; j < n2; j++) {
        if (b) {
          if (GeneralUtils.getPixel(src, i, j) == BLACK) {
            xNum[i]++;
          }
        } else {
          if (GeneralUtils.getPixel(src, j, i) == BLACK) {
            xNum[i]++;
          }
        }
      }
    }
    return xNum;
  }

  /**
   * 压缩像素值数量；即统计zipLine行像素值的数量为一行
   * Compress the number of pixel values; count the number of pixel values of zipLine rows as one row
   * @param num
   * @param zipLine
   */
  public static int[] zipLinePixel(int[] num, int zipLine) {
    int len = num.length / zipLine;
    int[] result = new int[len];
    int sum;
    for (int i = 0, j = 0; i < num.length && i + zipLine < num.length; i += zipLine) {
      sum = 0;
      for (int k = 0; k < zipLine; k++) {
        sum += num[i + k];
      }
      result[j++] = sum;
    }
    return result;
  }

  /**
   * 水平投影法切割，适用于类似表格的图像(默认白底黑字) 改进
   * Horizontal projection cutting, suitable for image like tables (default white background and black text) improvement
   * @param src Mat矩阵对象 - Mat object
   * @return
   */
  public static List<Mat> cutImgX(Mat src) {
    int i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int[] xNum, cNum;
    int average = 0; // 记录黑色像素和的平均值 - Record the average value of black pixels

    int zipLine = 3;
    // 压缩像素值数量；即统计三行像素值的数量为一行
    // Compress the number of pixel values;
    // 统计出每行黑色像素点的个数
    // count three rows of pixel values as one row
    xNum = zipLinePixel(countPixel(src, height, width, true), zipLine);

    // 排序 - Sort
    cNum = Arrays.copyOf(xNum, xNum.length);
    Arrays.sort(cNum);

    for (i = 31 * cNum.length / 32; i < cNum.length; i++) {
      average += cNum[i];
    }
    average /= (height / 32);

    // 把需要切割的y轴点存到cutY中
    // Store the y-axis points that need to be cut in cutY
    List<Integer> cutY = new ArrayList<Integer>();
    for (i = 0; i < xNum.length; i++) {
      if (xNum[i] > average) {
        cutY.add(i * zipLine + 1);
      }
    }

    // 优化cutY,把距离相差在30以内的都清除掉
    // Optimize cutY, remove all those whose distance is within 30
    if (cutY.size() != 0) {
      int temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
      // Optimize cutY because of the thickness of the line
      for (i = cutY.size() - 2; i >= 0; i--) {
        int k = temp - cutY.get(i);
        if (k <= 30) {
          cutY.remove(i + 1);
        } else {
          temp = cutY.get(i);
        }
      }
      temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
      // Optimize cutY because of the thickness of the line
      for (i = cutY.size() - 2; i >= 0; i--) {
        int k = temp - cutY.get(i);
        if (k <= 30) {
          cutY.remove(i + 1);
        } else {
          temp = cutY.get(i);
        }
      }
    }

    // 把切割的图片保存到YMat中
    // Store the cut images in YMat
    List<Mat> YMat = new ArrayList<Mat>();
    for (i = 1; i < cutY.size(); i++) {
      // // 设置感兴趣区域 - Set the region of interest
      int startY = cutY.get(i - 1);
      int h = cutY.get(i) - startY;
      Mat temp = new Mat(src, new Rect(0, startY, width, h));
      Mat t = new Mat();
      temp.copyTo(t);
      YMat.add(t);
    }
    return YMat;
  }

  /**
   * 垂直投影法切割，适用于类似表格的图像(默认白底黑字) 改进
   * Vertical projection cutting, suitable for image like tables (default white background and black text) improvement
   *
   * @param src Mat矩阵对象 - Mat object
   * @return
   */
  public static List<Mat> cutImgY(Mat src) {
    int i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int[] yNum, cNum;
    int average = 0; // 记录黑色像素和的平均值 - Record the average value of black pixels

    int zipLine = 2;
    // 压缩像素值数量；即统计三行像素值的数量为一行
    // Compress the number of pixel values
    // 统计出每列黑色像素点的个数
    // count three rows of pixel values as one row
    yNum = zipLinePixel(countPixel(src, width, height, false), zipLine);

    // 经过测试这样得到的平均值最优，平均值的选取很重要
    // After testing, this is the optimal way to get the average value. The selection of the average value is very important.
    cNum = Arrays.copyOf(yNum, yNum.length);
    Arrays.sort(cNum);
    for (i = 31 * cNum.length / 32; i < cNum.length; i++) {
      average += cNum[i];
    }
    average /= (cNum.length / 32);

    // 把需要切割的x轴的点存到cutX中
    // Store the x-axis points that need to be cut in cutX
    List<Integer> cutX = new ArrayList<Integer>();
    for (i = 0; i < yNum.length; i++) {
      if (yNum[i] >= average) {
        cutX.add(i * zipLine + 2);
      }
    }

    // 优化cutX - Optimize cutX
    if (cutX.size() != 0) {
      int temp = cutX.get(cutX.size() - 1);
      // 因为线条有粗细，优化cutX
      // Optimize cutX because of the thickness of the line
      for (i = cutX.size() - 2; i >= 0; i--) {
        int k = temp - cutX.get(i);
        if (k <= 100) {
          cutX.remove(i);
        } else {
          temp = cutX.get(i);
        }
      }
      temp = cutX.get(cutX.size() - 1);
      // 因为线条有粗细，优化cutX
      // Optimize cutX because of the thickness of the line
      for (i = cutX.size() - 2; i >= 0; i--) {
        int k = temp - cutX.get(i);
        if (k <= 100) {
          cutX.remove(i);
        } else {
          temp = cutX.get(i);
        }
      }
    }

    // 把切割的图片都保存到XMat中
    // Store the cut images in XMat
    List<Mat> XMat = new ArrayList<Mat>();
    for (i = 1; i < cutX.size(); i++) {
      // 设置感兴趣的区域
      // Set the region of interest
      int startX = cutX.get(i - 1);
      int w = cutX.get(i) - startX;
      Mat temp = new Mat(src, new Rect(startX, 0, w, height));
      Mat t = new Mat();
      temp.copyTo(t);
      XMat.add(t);
    }
    return XMat;
  }

  /**
   * 切割 因为是表格图像，采用新的切割思路，中和水平切割和垂直切割一次性切割出所有的小格子
   * Cutting. Because it is a table image, a new cutting idea is used, which cuts out all small cells in one step by combining horizontal and vertical cutting.
   *
   * @param src
   * @return
   */
  public static List<Mat> cut(Mat src) {
    if (src.channels() == 3) {
      // TODO
    }
    int i, j, k;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int[] xNum = new int[height], copy_xNum;
    int x_average = 0;
    int value = -1;
    // 统计每行每列的黑色像素值
    // Count the number of black pixels per row/column
    for (i = 0; i < width; i++) {
      for (j = 0; j < height; j++) {
        value = GeneralUtils.getPixel(src, j, i);
        if (value == BLACK) {
          xNum[j]++;
        }
      }
    }

    int zipXLine = 3;
    xNum = zipLinePixel(xNum, zipXLine);

    // 排序 ............求水平切割点
    // Sort ............ to get horizontal cutting points
    copy_xNum = Arrays.copyOf(xNum, xNum.length);
    Arrays.sort(copy_xNum);

    for (i = 31 * copy_xNum.length / 32; i < copy_xNum.length; i++) {
      x_average += copy_xNum[i];
    }
    x_average /= (height / 32);

    // 把需要切割的y轴点存到cutY中
    // Store the y-axis points that need to be cut in cutY
    List<Integer> cutY = new ArrayList<Integer>();
    for (i = 0; i < xNum.length; i++) {
      if (xNum[i] > x_average) {
        cutY.add(i * zipXLine + zipXLine / 2);
      }
    }

    // 优化cutY,把距离相差在30以内的都清除掉
    // Optimize cutY, remove all those whose distance is within 30
    if (cutY.size() != 0) {
      int temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
      // Optimize cutY because of the thickness of the line
      for (i = cutY.size() - 2; i >= 0; i--) {
        k = temp - cutY.get(i);
        if (k <= 10 * zipXLine) {
          cutY.remove(i + 1);
        } else {
          temp = cutY.get(i);
        }
      }
      temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
      // Optimize cutY because of the thickness of the line
      for (i = cutY.size() - 2; i >= 0; i--) {
        k = temp - cutY.get(i);
        if (k <= 10 * zipXLine) {
          cutY.remove(i + 1);
        } else {
          temp = cutY.get(i);
        }
      }
    }

    // 把需要切割的x轴的点存到cutX中
    // Store the x-axis points that need to be cut in cutX
    // 新思路，因为不是很畸变的图像，y轴的割点还是比较好确定的 随机的挑选一个y轴割点，用一个滑动窗口去遍历选中点所在直线，确定x轴割点
    // New idea, because it is not a very distorted image, the y-axis cutting point is still relatively easy to determine.
    // Randomly select a y-axis cutting point, use a sliding window to traverse the straight line where the selected point is located, and determine the x-axis cutting point.
    List<Integer> cutX = new ArrayList<Integer>();
    int choiceY = cutY.size() > 1 ? cutY.get(1) : (cutY.size() > 0 ? cutY.get(0) : -1);
    if (choiceY == -1) {
      throw new RuntimeException("切割失败，没有找到水平切割点 - Cutting failed, no horizontal cutting point found");
    }

    int winH = 5;
    List<Integer> LH1 = new ArrayList<Integer>();
    List<Integer> LH2 = new ArrayList<Integer>();
    if (choiceY - winH >= 0 && choiceY + winH <= height) {
      // 上下 - Up and down
      for (i = 0; i < width; i++) {
        value = GeneralUtils.getPixel(src, choiceY - winH, i);
        if (value == BLACK) {
          LH1.add(i);
        }
        value = GeneralUtils.getPixel(src, choiceY + winH, i);
        if (value == BLACK) {
          LH2.add(i);
        }
      }
    } else if (choiceY + winH <= height && choiceY + 2 * winH <= height) {
      // 下 - Down
      for (i = 0; i < width; i++) {
        value = GeneralUtils.getPixel(src, choiceY + 2 * winH, i);
        if (value == BLACK) {
          LH1.add(i);
        }
        value = GeneralUtils.getPixel(src, choiceY + winH, i);
        if (value == BLACK) {
          LH2.add(i);
        }
      }
    } else if (choiceY - winH >= 0 && choiceY - 2 * winH >= 0) {
      // 上 - Up
      for (i = 0; i < width; i++) {
        value = GeneralUtils.getPixel(src, choiceY - winH, i);
        if (value == BLACK) {
          LH1.add(i);
        }
        value = GeneralUtils.getPixel(src, choiceY - 2 * winH, i);
        if (value == BLACK) {
          LH2.add(i);
        }
      }
    } else {
      throw new RuntimeException("切割失败，图像异常 - Cutting failed, image exception");
    }

    // 优化LH1、LH2,把距离相差在30以内的都清除掉
    // Optimize LH1 and LH2, removing all distances within 30
    if (LH1.size() != 0) {
      int temp = LH1.get(LH1.size() - 1);
      // 因为线条有粗细，优化cutY
      // optimize cutY because of line thickness
      for (i = LH1.size() - 2; i >= 0; i--) {
        k = temp - LH1.get(i);
        if (k <= 50) {
          LH1.remove(i + 1);
        } else {
          temp = LH1.get(i);
        }
      }
      temp = LH1.get(LH1.size() - 1);
      // 因为线条有粗细，优化cutY
      // optimize cutY because of line thickness
      for (i = LH1.size() - 2; i >= 0; i--) {
        k = temp - LH1.get(i);
        if (k <= 50) {
          LH1.remove(i + 1);
        } else {
          temp = LH1.get(i);
        }
      }
    }
    if (LH2.size() != 0) {
      int temp = LH2.get(LH2.size() - 1);
      // 因为线条有粗细，优化cutY
      // optimize cutY because of line thickness
      for (i = LH2.size() - 2; i >= 0; i--) {
        k = temp - LH2.get(i);
        if (k <= 50) {
          LH2.remove(i + 1);
        } else {
          temp = LH2.get(i);
        }
      }
      temp = LH2.get(LH2.size() - 1);
      // 因为线条有粗细，优化cutY
      // optimize cutY because of line thickness
      for (i = LH2.size() - 2; i >= 0; i--) {
        k = temp - LH2.get(i);
        if (k <= 50) {
          LH2.remove(i + 1);
        } else {
          temp = LH2.get(i);
        }
      }
    }

    if (LH1.size() < LH2.size()) {
      // 进一步优化LH1
      // further optimize LH1
      int avg = 0;
      for (k = 1; k < LH1.size() - 2; k++) {
        avg += LH1.get(k + 1) - LH1.get(k);
      }
      avg /= (LH1.size() - 2);

      int temp = LH1.get(LH1.size() - 1);
      for (i = LH1.size() - 2; i >= 0; i--) {
        k = temp - LH1.get(i);
        if (k <= avg) {
          LH1.remove(i + 1);
        } else {
          temp = LH1.get(i);
        }
      }
      cutX = LH1;
    } else {
      // 进一步优化LH2
      //  further optimize LH2
      int avg = 0;
      for (k = 1; k < LH2.size() - 2; k++) {
        avg += LH2.get(k + 1) - LH2.get(k);
      }
      avg /= (LH2.size() - 2);

      int temp = LH2.get(LH2.size() - 1);
      for (i = LH2.size() - 2; i >= 0; i--) {
        k = temp - LH2.get(i);
        if (k <= avg) {
          LH2.remove(i + 1);
        } else {
          temp = LH2.get(i);
        }
      }
      cutX = LH2;
    }

    List<Mat> destMat = new ArrayList<Mat>();
    for (i = 1; i < cutY.size(); i++) {
      for (j = 1; j < cutX.size(); j++) {
        // 设置感兴趣的区域
        // set region of interest
        int startX = cutX.get(j - 1);
        int w = cutX.get(j) - startX;
        int startY = cutY.get(i - 1);
        int h = cutY.get(i) - startY;
        Mat temp = new Mat(src, new Rect(startX + 2, startY + 2, w - 2, h - 2));
        Mat t = new Mat();
        temp.copyTo(t);
        destMat.add(t);
      }
    }

    return destMat;
  }
}
