package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;

/** 降噪工具类 */
public class NoiseUtils {
  private static final int WHITE = 255;
  
  /**
   * 作用：给单通道的图像边缘预处理，降噪(默认白底黑字)
   *
   * @param src Mat矩阵对象
   * @return
   */
  public static Mat strokeWhite(Mat src) {
    if (src.channels() != 1) {
      throw new RuntimeException("不是单通道图，需要先灰度化");
    }
    int i, width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    for (i = 0; i < width; i++) {
      GeneralUtils.setPixel(src, i, 0, WHITE);
      GeneralUtils.setPixel(src, i, height - 1, WHITE);
    }
    for (i = 0; i < height; i++) {
      GeneralUtils.setPixel(src, 0, i, WHITE);
      GeneralUtils.setPixel(src, width - 1, i, WHITE);
    }
    return src;
  }

  
  /**
   * 8邻域降噪，又有点像9宫格降噪;即如果9宫格中心被异色包围，则同化 作用：降噪(默认白底黑字)
   *
   * @param src Mat矩阵对象
   * @param pNum 阀值 默认取1即可
   * @return
   */
  public static Mat navieRemoveNoise(Mat src, int pNum) {
    int i, j, m, n, nValue, nCount;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

    // 如果一个点的周围都是白色的，自己确实黑色的，同化
    for (j = 1; j < height - 1; j++) {
      for (i = 1; i < width - 1; i++) {
        nValue = GeneralUtils.getPixel(src, j, i);
        if (nValue == 0) {
          nCount = 0;
          // 比较(j , i)周围的9宫格，如果周围都是白色，同化
          for (m = j - 1; m <= j + 1; m++) {
            for (n = i - 1; n <= i + 1; n++) {
              if (GeneralUtils.getPixel(src, m, n) == 0) {
                nCount++;
              }
            }
          }
          if (nCount <= pNum) {
            // 周围黑色点的个数小于阀值pNum,把自己设置成白色
            GeneralUtils.setPixel(src, j, i, GeneralUtils.getWHITE());
          }
        } else {
          nCount = 0;
          // 比较(j , i)周围的9宫格，如果周围都是黑色，同化
          for (m = j - 1; m <= j + 1; m++) {
            for (n = i - 1; n <= i + 1; n++) {
              if (GeneralUtils.getPixel(src, m, n) == 0) {
                nCount++;
              }
            }
          }
          if (nCount >= 8 - pNum) {
            // 周围黑色点的个数大于等于(8 - pNum),把自己设置成黑色
            GeneralUtils.setPixel(src, j, i, GeneralUtils.getBLACK());
          }
        }
      }
    }
    return src;
  }

  /**
   * 连通域降噪 作用：降噪(默认白底黑字)
   *
   * @param src Mat矩阵对象
   * @param pArea 阀值 默认取1即可
   * @return
   */
  public static Mat connectedRemoveNoise(Mat src, double pArea) {
    int i, j, color = 1;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

    Result result = floodFill(new Result(src), pArea);
    src = result.mat;

    // 二值化
    for (i = 0; i < width; i++) {
      for (j = 0; j < height; j++) {
        if (GeneralUtils.getPixel(src, j, i) < GeneralUtils.getWHITE()) {
          GeneralUtils.setPixel(src, j, i, GeneralUtils.getBLACK());
        }
      }
    }

    if (result.status == false && result.count <= 100) {
      connectedRemoveNoise(src, pArea);
    }

    return src;
  }

  /**
   * 连通域填充颜色
   *
   * @param result
   * @return
   */
  public static Result floodFill(Result result, double pArea) {
    Mat src = result.mat;
    if (src == null) {
      return null;
    }
    int i, j, color = 1;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

    for (i = 0; i < width; i++) {
      for (j = 0; j < height; j++) {
        if (GeneralUtils.getPixel(src, j, i) == GeneralUtils.getBLACK()) {
          // 用不同的颜色填充连接区域中的每个黑色点
          // floodFill就是把与点(i , j)的所有相连通的区域都涂上color颜色
          int area = opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(color));
          if (area <= pArea) {
            opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(255));
          } else {
            color++;
          }
          if (color == 255) {
            result.status = false; // 连通域还没填充完
            result.mat = src;
            result.count = result.count + 1;
            return result;
          }
        }
      }
    }
    result.mat = src;
    result.status = true; // 表示所有的连通域都已填充完毕
    return result;
  }

  /**
   * 连通域填充颜色
   *
   * @param src
   * @return
   */
  public static Mat floodFill(Mat src, double pArea) {
    if (src == null) {
      return null;
    }
    int i, j, color = 1;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

    for (i = 0; i < width; i++) {
      for (j = 0; j < height; j++) {
        if (GeneralUtils.getPixel(src, j, i) == GeneralUtils.getBLACK()) {
          // 用不同的颜色填充连接区域中的每个黑色点
          // floodFill就是把与点(i , j)的所有相连通的区域都涂上color颜色
          int area = opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(color));
          if (area <= pArea) {
            System.out.println(color);
            opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(255));
          } else {
            color++;
          }
          System.out.println(color);
        }
      }
    }
    return src;
  }

  // 只填充最大的连通域
  public static Mat findMaxConnected(Mat src) {
    int i, j, color = 127;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int maxArea = Integer.MAX_VALUE;
    int maxI = -1, maxJ = -1;
    for (i = 0; i < width; i++) {
      for (j = 0; j < height; j++) {
        if (GeneralUtils.getPixel(src, j, i) == GeneralUtils.getBLACK()) {
          // 用不同的颜色填充连接区域中的每个黑色点
          // floodFill就是把与点(i , j)的所有相连通的区域都涂上color颜色
          int area = opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(color));
          if (maxI != -1 && maxJ != -1) {
            if (area > maxArea) {
              maxArea = area;
              opencv_imgproc.floodFill(src, new Mat(), new Point(maxI, maxJ), new Scalar(255));
              maxI = i;
              maxJ = j;
            } else {
              opencv_imgproc.floodFill(src, new Mat(), new Point(i, j), new Scalar(255));
            }
          } else {
            maxI = i;
            maxJ = j;
            maxArea = area;
          }
        }
      }
    }
    return src;
  }

  private static class Result {
    Mat mat; // Mat对象
    boolean status; // 是否填充完毕
    int count; // 记录填充的次数
    int height; // 记录上一次填充的height位置
    int width; // 记录上一次填充的width位置

    public Result() {}

    public Result(Mat src) {
      this.mat = src;
    }
  }
}
