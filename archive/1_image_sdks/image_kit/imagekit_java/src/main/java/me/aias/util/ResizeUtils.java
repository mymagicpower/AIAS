package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * 归一化工具类
 * Normalization utility class
 */

public class ResizeUtils {
  // 设置归一化图像的固定大小
  // Set the fixed size of the normalized image
  private static final Size defaultDsize = new Size(32, 32);

  /**
   * 把图片归一化到相同的大小
   * Normalize the image to the same size
   * @param src Mat矩阵对象 - Mat matrix object
   * @return
   */
  public static Mat resize(Mat src) {
    return resize(src, defaultDsize);
  }

  /**
   * 把图片归一化到相同的大小
   * Normalize the image to the same size
   *
   * @param src Mat矩阵对象 - Mat matrix object
   * @return
   */
  public static Mat resize(Mat src, Size dsize) {
    src = trimImg(src);
    Mat dst = new Mat();
    // 区域插值(INTER_AREA):图像放大时类似于线性插值，图像缩小时可以避免波纹出现。
    // Area interpolation (INTER_AREA): similar to linear interpolation when the image is enlarged, and can avoid ripples when the image is reduced.
    opencv_imgproc.resize(src, dst, dsize, 0, 0, Imgproc.INTER_AREA);
    return dst;
  }

  /**
   * 去除图像中的空白
   * Remove the blank space in the image
   *
   * @param src
   * @return
   */
  public static Mat trimImg(Mat src) {
    List<Double> colList = MathUtils.avgColMat(src); // 每一列的平均值 - Average value of each column
    List<Double> rowList = MathUtils.avgRowMat(src); // 每一行的平均值 - Average value of each row

    double colAvg = MathUtils.getSumInList(colList) / colList.size();
    double rowAvg = MathUtils.getSumInList(rowList) / rowList.size();

    int blankCol1 = -1; // 空白列的关键分割点(左) - Key segmentation point (left) of blank column
    int blankCol2 = -1; // 空白列的关键分割点(右) - Key segmentation point (right) of blank column
    int blankRow1 = -1; // 空白行的关键分割点(上) - Key segmentation point (up) of blank row
    int blankRow2 = -1; // 空白行的关键分割点(下) - Key segmentation point (down) of blank row

    int preValue = -1;
    int curValue = -1;
    int count = 0;
    boolean b = true;
    for (int i = 0; i < colList.size(); i++) {
      if (b == false) {
        break;
      }
      if (colList.get(i) > colAvg) {
        // 求空白列的关键分割点(左) - Calculate the key segmentation point (left) of the blank column
        curValue = i;
        if (preValue != -1) {
          if (curValue - preValue == 1) {
            // 连续 - Continuous
            count++;
          } else {
            // 不连续 - Not continuous
            if (count > 10) {
              blankCol1 = 2 * i / 3;
              b = false;
            }
          }
        }
        preValue = i;
      }
    }

    preValue = -1;
    curValue = -1;
    count = 0;
    b = true;
    for (int i = colList.size() - 1; i >= 0; i--) {
      if (b == false) {
        break;
      }
      if (colList.get(i) > colAvg) {
        // 求空白列的关键分割点(右)
        // Calculate the key segmentation point (right) of the blank column
        curValue = i;
        if (preValue != -1) {
          if (curValue - preValue == -1) {
            // 连续  - Continuous
            count++;
          } else {
            // 不连续 - Not continuous
            if (count > 10) {
              blankCol2 = i + (colList.size() - i) / 3;
              b = false;
            }
          }
        }
        preValue = i;
      }
    }

    preValue = -1;
    curValue = -1;
    count = 0;
    b = true;
    for (int i = 0; i < rowList.size(); i++) {
      if (rowList.get(i) > rowAvg) {
        // 空白行的关键分割点(上)
        // Key segmentation point (up) of blank row
        if (b == false) {
          break;
        }
        if (rowList.get(i) > rowAvg) {
          curValue = i;
          if (preValue != -1) {
            if (curValue - preValue == 1) {
              // 连续 - Continuous
              count++;
            } else {
              // 不连续 - Not continuous
              if (count > 10) {
                blankRow1 = i / 2;
                b = false;
              }
            }
          }
          preValue = i;
        }
      }
    }

    preValue = -1;
    curValue = -1;
    count = 0;
    b = true;
    for (int i = rowList.size() - 1; i >= 0; i--) {
      if (rowList.get(i) > rowAvg) {
        // 空白行的关键分割点(下)
        // Key segmentation point (down) of blank row
        if (b == false) {
          break;
        }
        if (rowList.get(i) > rowAvg) {
          curValue = i;
          if (preValue != -1) {
            if (curValue - preValue == -1) {
              // 连续 - Continuous
              count++;
            } else {
              // 不连续 - Not continuous
              if (count > 10) {
                blankRow2 = i + 2 * (rowList.size() - i) / 3;
                b = false;
              }
            }
          }
          preValue = i;
        }
      }
    }

    // int blankCol1 = -1; 空白列的关键分割点(左) - Key splitting point (left) of the blank column
    // int blankCol2 = -1; 空白列的关键分割点(右) - Key splitting point (right) of the blank column
    // int blankRow1 = -1; 空白行的关键分割点(上) - Key splitting point (top) of the blank row
    // int blankRow2 = -1; 空白行的关键分割点(下) - Key splitting point (bottom) of the blank row
    // Select the area of interest

    blankCol1 = blankCol1 == -1 ? 0 : blankCol1;
    blankCol2 = blankCol2 == -1 ? colList.size() : blankCol2;
    blankRow1 = blankRow1 == -1 ? 0 : blankRow1;
    blankRow2 = blankRow2 == -1 ? rowList.size() : blankRow2;

    Mat temp =
        new Mat(
            src, new Rect(blankCol1, blankRow1, blankCol2 - blankCol1, blankRow2 - blankRow1));
    Mat t = new Mat();
    temp.copyTo(t);

    return t;
  }
}
