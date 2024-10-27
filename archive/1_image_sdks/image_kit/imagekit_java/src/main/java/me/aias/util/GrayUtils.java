package me.aias.util;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.List;
import java.util.Random;

/** 对图像灰度化处理的工具类 默认有效数据的灰度值小于底色，即有效数据更清晰 */
public class GrayUtils {
  /**
   * 作用：灰度化
   *
   * @param src
   * @return
   */
  public static Mat grayNative(Mat src) {
    Mat gray = src.clone();
    if (src.channels() == 3) {
      // 进行灰度化
      opencv_imgproc.cvtColor(src, gray, opencv_imgproc.COLOR_BGR2GRAY);
      src = gray;
    }
    return src;
  }

  /**
   * 均值灰度化减噪 细粒度灰度化，只降低噪声，不对有效数据做任何的加强的处理 根据灰度化后的图像每一列的像素值的平均值(默认)或者其他表达式值作为阀值，把大于阀值的像素都改为255
   * 可以在一定程度上降低噪声，而不对有效数据造成任何影响
   *
   * @param src
   * @return
   */
  public static Mat grayColByMidle(Mat src) {
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    List<List<Double>> data = MathUtils.MatPixelToList(src);
    return grayColByMidle(src, data);
  }

  /**
   * 根据灰度化后的图像每一列的像素值的平均值作为阀值，把大于阀值的像素都改为255
   *
   * @param data List<Double>中存储图像灰度化后的每一列的像素值
   * @return
   */
  public static Mat grayColByMidle(Mat src, List<List<Double>> data) {
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    UByteRawIndexer ldIdx = src.createIndexer();
    //        IntRawIndexer ldIdx = src.createIndexer();

    for (int j = 0; j < data.size(); j++) {
      List<Double> list = data.get(j);

      int avg = (int) ((MathUtils.getSumInList(list) / list.size()) * 0.95);

      // 随机的更新像素值
      int count = 3 * list.size() / 4;
      int time = 0;
      while (count > 0) {

        int index = new Random().nextInt(list.size());

        if (list.get(index) >= avg) {
          ldIdx.put(index, j, 255);
          count--;
        }

        if (count == count) {
          time++;
        }
        // 避免程序进入死循环
        if (time == list.size() / 2) {
          break;
        }
      }
    }
    ldIdx.release();
    return src;
  }

  /**
   * k值灰度化减噪 根据灰度化后的图像每一列的像素值的第k大值作为阀值，把大于阀值的像素都改为255 默认选取第1/3大的值作为阀值
   *
   * @param src
   * @return
   */
  public static Mat grayColByKLargest(Mat src) {
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    List<List<Double>> data = MathUtils.MatPixelToList(src);
    return grayColByKLargest(src, 3, data);
  }

  /**
   * 根据灰度化后的图像每一列的像素值的第k大值作为阀值，把大于阀值的像素都改为255
   *
   * @param src
   * @param k 分母
   * @return
   */
  public static Mat grayColByKLargest(Mat src, int k) {
    if (k == 0) {
      throw new RuntimeException("k不能为0");
    }
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    List<List<Double>> data = MathUtils.MatPixelToList(src);
    return grayColByKLargest(src, k, data);
  }

  /**
   * 根据灰度化后的图像每一列的像素值的第k大值作为阀值，把大于阀值的像素都改为255
   *
   * @param src
   * @param k
   * @param data
   * @return
   */
  public static Mat grayColByKLargest(Mat src, int k, List<List<Double>> data) {
    if (k == 0) {
      throw new RuntimeException("k不能为0");
    }
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    UByteRawIndexer ldIdx = src.createIndexer();
    //        IntRawIndexer ldIdx = src.createIndexer();

    for (int j = 0; j < data.size(); j++) {
      List<Double> list = data.get(j);
      Object[] doubles = list.toArray();
      Double[] doubles1 = new Double[doubles.length];
      for (int i = 0; i < doubles.length; i++) {
        doubles1[i] = (Double) doubles[i];
      }
      double d = MathUtils.findKthLargest(doubles1, list.size() / k);

      // 随机的更新像素值
      int count = 3 * doubles.length / 4;
      int time = 0;
      while (count > 0) {

        int index = new Random().nextInt(list.size());

        if (list.get(index) >= d) {
          ldIdx.put(index, j, 255);
          count--;
        }

        if (count == count) {
          time++;
        }
        // 避免程序进入死循环
        if (time == list.size() / 2) {
          break;
        }
      }
    }
    ldIdx.release();
    return src;
  }

  /**
   * 作用：自适应选取阀值
   *
   * @param src Mat矩阵图像
   * @return
   */
  public static int getAdapThreshold(Mat src) {
    int threshold = 0, threshold_new = 127;
    int nWhite_count, nBlack_count;
    int nWhite_sum, nBlack_sum;
    int value, i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);

    if (width == 0 || height == 0) {
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
      if (nWhite_count == 0 || nBlack_count == 0) {
        threshold_new = (nWhite_sum + nBlack_sum) / (nWhite_count + nBlack_count);
      } else {
        threshold_new = (nWhite_sum / nWhite_count + nBlack_sum / nBlack_count) / 2;
      }
    }

    return threshold;
  }

  /**
   * 局部自适应阀值灰度化降噪
   *
   * @param src
   * @return
   */
  public static Mat grayColByPartAdapThreshold(Mat src) {
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    int width = GeneralUtils.getImgWidth(src);
    int height = GeneralUtils.getImgHeight(src);
    int value;
    for (int i = 0; i < width; i++) {
      Mat partMat = src.col(i);
      int thresold = getAdapThreshold(partMat);
      for (int j = 0; j < height; j++) {
        value = GeneralUtils.getPixel(src, j, i);
        if (value > thresold) {
          GeneralUtils.setPixel(src, j, i, GeneralUtils.getWHITE());
        }
      }
    }
    return src;
  }

  /**
   * 全局自适应阀值灰度化降噪
   *
   * @param src
   * @return
   */
  public static Mat grayColByAdapThreshold(Mat src) {
    if (src.channels() != 1) {
      src = grayNative(src);
    }
    int i, j;
    int width = GeneralUtils.getImgWidth(src), height = GeneralUtils.getImgHeight(src);
    int value;

    int threshold = getAdapThreshold(src);
    for (j = 0; j < height; j++) {
      for (i = 0; i < width; i++) {
        value = GeneralUtils.getPixel(src, j, i);
        if (value > threshold) {
          GeneralUtils.setPixel(src, j, i, GeneralUtils.getWHITE());
        }
      }
    }

    return src;
  }
}
