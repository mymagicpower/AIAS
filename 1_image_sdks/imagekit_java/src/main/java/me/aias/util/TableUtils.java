package me.aias.util;

import org.bytedeco.opencv.opencv_core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像处理工具类1.0.0(只针对几乎没有畸变的图像) 灰度话、二值化、降噪、切割、归一化
 *
 * @author admin
 */
public class TableUtils {
  private static final int BLACK = 0;

  // 私有化构造函数
  private TableUtils() {};

  /**
   * 统计图像每行/每列黑色像素点的个数
   * (n1,n2)=>(height,width),
   * b=true;统计每行
   * (n1,n2)=>(width,height),
   * b=false;统计每列
   *
   * @param src Mat矩阵对象
   * @param n1
   * @param n2
   * @param b true表示统计每行;false表示统计每列
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
   *
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
   *
   * @param src Mat矩阵对象
   * @return
   */
  public static List<Mat> cutImgX(Mat src) {
    int i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int[] xNum, cNum;
    int average = 0; // 记录黑色像素和的平均值

    int zipLine = 3;
    // 压缩像素值数量；即统计三行像素值的数量为一行// 统计出每行黑色像素点的个数
    xNum = zipLinePixel(countPixel(src, height, width, true), zipLine);

    // 排序
    cNum = Arrays.copyOf(xNum, xNum.length);
    Arrays.sort(cNum);

    for (i = 31 * cNum.length / 32; i < cNum.length; i++) {
      average += cNum[i];
    }
    average /= (height / 32);

    // System.out.println(average);

    // 把需要切割的y轴点存到cutY中
    List<Integer> cutY = new ArrayList<Integer>();
    for (i = 0; i < xNum.length; i++) {
      if (xNum[i] > average) {
        cutY.add(i * zipLine + 1);
      }
    }

    // 优化cutY,把距离相差在30以内的都清除掉
    if (cutY.size() != 0) {
      int temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
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
      for (i = cutY.size() - 2; i >= 0; i--) {
        int k = temp - cutY.get(i);
        if (k <= 30) {
          cutY.remove(i + 1);
        } else {
          temp = cutY.get(i);
        }
      }
    }

    // // 把切割的图片保存到YMat中
    List<Mat> YMat = new ArrayList<Mat>();
    for (i = 1; i < cutY.size(); i++) {
      // // 设置感兴趣区域
      int startY = cutY.get(i - 1);
      int h = cutY.get(i) - startY;
      // System.out.println(startY);
      // System.out.println(h);
      Mat temp = new Mat(src, new Rect(0, startY, width, h));
      Mat t = new Mat();
      temp.copyTo(t);
      YMat.add(t);
    }
    return YMat;
  }

  /**
   * 垂直投影法切割，适用于类似表格的图像(默认白底黑字) 改进
   *
   * @param src Mat矩阵对象
   * @return
   */
  public static List<Mat> cutImgY(Mat src) {
    int i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int[] yNum, cNum;
    int average = 0; // 记录黑色像素和的平均值

    int zipLine = 2;
    // 压缩像素值数量；即统计三行像素值的数量为一行// 统计出每列黑色像素点的个数
    yNum = zipLinePixel(countPixel(src, width, height, false), zipLine);

    // 经过测试这样得到的平均值最优，平均值的选取很重要
    cNum = Arrays.copyOf(yNum, yNum.length);
    Arrays.sort(cNum);
    for (i = 31 * cNum.length / 32; i < cNum.length; i++) {
      average += cNum[i];
    }
    average /= (cNum.length / 32);

    // 把需要切割的x轴的点存到cutX中
    List<Integer> cutX = new ArrayList<Integer>();
    for (i = 0; i < yNum.length; i++) {
      if (yNum[i] >= average) {
        cutX.add(i * zipLine + 2);
      }
    }

    // 优化cutX
    if (cutX.size() != 0) {
      int temp = cutX.get(cutX.size() - 1);
      // 因为线条有粗细，优化cutX
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
    List<Mat> XMat = new ArrayList<Mat>();
    for (i = 1; i < cutX.size(); i++) {
      // 设置感兴趣的区域
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
    copy_xNum = Arrays.copyOf(xNum, xNum.length);
    Arrays.sort(copy_xNum);

    for (i = 31 * copy_xNum.length / 32; i < copy_xNum.length; i++) {
      x_average += copy_xNum[i];
    }
    x_average /= (height / 32);

    // System.out.println("x_average: " + x_average);

    // 把需要切割的y轴点存到cutY中
    List<Integer> cutY = new ArrayList<Integer>();
    for (i = 0; i < xNum.length; i++) {
      if (xNum[i] > x_average) {
        cutY.add(i * zipXLine + zipXLine / 2);
      }
    }

    // 优化cutY,把距离相差在30以内的都清除掉
    if (cutY.size() != 0) {
      int temp = cutY.get(cutY.size() - 1);
      // 因为线条有粗细，优化cutY
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
    /** 新思路，因为不是很畸变的图像，y轴的割点还是比较好确定的 随机的挑选一个y轴割点，用一个滑动窗口去遍历选中点所在直线，确定x轴割点 */
    List<Integer> cutX = new ArrayList<Integer>();
    int choiceY = cutY.size() > 1 ? cutY.get(1) : (cutY.size() > 0 ? cutY.get(0) : -1);
    if (choiceY == -1) {
      throw new RuntimeException("切割失败，没有找到水平切割点");
    }

    int winH = 5;
    List<Integer> LH1 = new ArrayList<Integer>();
    List<Integer> LH2 = new ArrayList<Integer>();
    if (choiceY - winH >= 0 && choiceY + winH <= height) {
      // 上下
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
      // 下
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
      // 上
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
      throw new RuntimeException("切割失败，图像异常");
    }

    // 优化LH1、LH2,把距离相差在30以内的都清除掉
    if (LH1.size() != 0) {
      int temp = LH1.get(LH1.size() - 1);
      // 因为线条有粗细，优化cutY
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
