package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.RotatedRect;

import static org.bytedeco.opencv.global.opencv_imgproc.*;

/** 轮廓工具类 */
public class ContourUtils {

  /**
   * 寻找轮廓
   *
   * @param cannyMat
   * @return
   */
  public static MatVector getContours(Mat cannyMat) {
    MatVector contours = new MatVector();
    Mat hierarchy = new Mat();

    // 寻找轮廓
    // CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE
    // CV_RETR_TREE
    findContours(
        cannyMat,
        contours,
        hierarchy,
        opencv_imgproc.RETR_LIST,
        opencv_imgproc.CHAIN_APPROX_SIMPLE,
        new Point(0, 0));

    if (contours.size() <= 0) {
      throw new RuntimeException("未找到图像轮廓");
    } else {
      return contours;
    }
  }

  /**
   * 作用：返回边缘检测之后的最大轮廓
   *
   * @param contours
   * @return
   */
  public static Mat getMaxContour(MatVector contours) {
    // 找出匹配到的最大轮廓
    double area = boundingRect(contours.get(0)).area();
    int index = 0;

    // 找出匹配到的最大轮廓
    for (int i = 0; i < contours.size(); i++) {
      double tempArea = boundingRect(contours.get(i)).area();
      if (tempArea > area) {
        area = tempArea;
        index = i;
      }
    }
    return contours.get(index);
  }

  /**
   * 作用：返回边缘检测之后的最大轮廓
   *
   * @param cannyMat Canny之后的Mat矩阵
   * @return
   */
  public static Mat getMaxContour(Mat cannyMat) {
    MatVector contours = getContours(cannyMat);
    // 找出匹配到的最大轮廓
    double area = boundingRect(contours.get(0)).area();
    int index = 0;

    // 找出匹配到的最大轮廓
    for (int i = 0; i < contours.size(); i++) {
      double tempArea = boundingRect(contours.get(i)).area();
      if (tempArea > area) {
        area = tempArea;
        index = i;
      }
    }
    return contours.get(index);
  }

  /**
   * 利用函数approxPolyDP来对指定的点集进行逼近 精确度设置好，效果还是比较好的
   *
   * @param cannyMat
   */
  public static Mat useApproxPolyDPFindPoints(Mat cannyMat) {
    return useApproxPolyDPFindPoints(cannyMat, 0.01);
  }

  /**
   * 利用函数approxPolyDP来对指定的点集进行逼近 精确度设置好，效果还是比较好的
   *
   * @param cannyMat
   * @param threshold 阀值(精确度)
   * @return
   */
  public static Mat useApproxPolyDPFindPoints(Mat cannyMat, double threshold) {

    Mat maxContour = getMaxContour(cannyMat);
    Mat approxCurve = new Mat();

    // 原始曲线与近似曲线之间的最大距离设置为0.01，true表示是闭合的曲线
    opencv_imgproc.approxPolyDP(maxContour, approxCurve, threshold, true);
    return approxCurve;
  }
}
