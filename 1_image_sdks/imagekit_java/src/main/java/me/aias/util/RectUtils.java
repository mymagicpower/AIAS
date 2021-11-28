package me.aias.util;

import org.bytedeco.opencv.opencv_core.*;

import static org.bytedeco.opencv.global.opencv_imgproc.minAreaRect;

/** 矩形框工具类 */
public class RectUtils {

  /**
   * 返回边缘检测之后的最大矩形
   *
   * @param contours
   * @return
   */
  public static RotatedRect getMaxRect(MatVector contours) {
    // 找出匹配到的最大轮廓
    Mat maxContour = ContourUtils.getMaxContour(contours);
    RotatedRect rect = minAreaRect(maxContour);
    return rect;
  }

  /**
   * 返回边缘检测之后的最大矩形
   *
   * @param cannyMat Canny之后的mat矩阵
   * @return
   */
  public static RotatedRect getMaxRect(Mat cannyMat) {
    // 找出匹配到的最大轮廓
    Mat maxContour = ContourUtils.getMaxContour(cannyMat);
    RotatedRect rect = minAreaRect(maxContour);
    return rect;
  }
  
  /**
   * 把矫正后的图像切割出来
   *
   * @param correctMat 图像矫正后的Mat矩阵
   */
  public static Mat cutRect(Mat correctMat, Mat nativeCorrectMat) {
    // 获取所有轮廓
    MatVector contours = ContourUtils.getContours(correctMat);

    // 获取最大矩形
    RotatedRect rect = getMaxRect(contours);

    Point2f rectPoint = new Point2f(4);
    rect.points(rectPoint);

    for (int i = 0; i < rectPoint.limit(); i++) {
      System.out.println(rectPoint.position(i).x() + " , " + rectPoint.position(i).y());
    }

    int x = rect.boundingRect().x();
    int y = rect.boundingRect().y();
    int width = rect.boundingRect().width();
    int height = rect.boundingRect().height();
    //        int x = (int) Math.abs(rectPoint.position(2).x());
    //        int y = (int) Math.abs(rectPoint.position(2).y() < rectPoint.position(3).y() ?
    // rectPoint.position(2).y() : rectPoint.position(3).y());
    //        int width = (int) Math.abs(rectPoint.position(3).x() - rectPoint.position(2).x());
    //        int height = (int) Math.abs(rectPoint.position(1).y() - rectPoint.position(2).y());

    System.out.println("startLeft = " + x);
    System.out.println("startUp = " + y);
    System.out.println("width = " + width);
    System.out.println("height = " + height);

    // TODO
    //        int[] roi = cutRectHelp(rectPoint);
    //        Mat temp = new Mat(nativeCorrectMat, new Rect(roi[0], roi[1], roi[2], roi[3]));

    Mat temp = new Mat(nativeCorrectMat, new Rect(x, y, width, height));
    Mat t = new Mat();
    temp.copyTo(t);

    return t;
  }

  /**
   * 把矫正后的图像切割出来--辅助函数(修复)
   *
   * @param rectPoint 矩形的四个点
   * @return int[startLeft , startUp , width , height]
   */
  public static int[] cutRectHelp(Point2f rectPoint) {
    double minX = rectPoint.position(0).x();
    double maxX = rectPoint.position(0).x();
    double minY = rectPoint.position(0).y();
    double maxY = rectPoint.position(0).y();
    for (int i = 1; i < 4; i++) {
      minX = rectPoint.position(i).x() < minX ? rectPoint.position(i).x() : minX;
      maxX = rectPoint.position(i).x() > maxX ? rectPoint.position(i).x() : maxX;
      minY = rectPoint.position(i).y() < minY ? rectPoint.position(i).y() : minY;
      maxY = rectPoint.position(i).y() > maxY ? rectPoint.position(i).y() : maxY;
    }
    int[] roi = {
            (int) Math.abs(minX),
            (int) Math.abs(minY),
            (int) Math.abs(maxX - minX),
            (int) Math.abs(maxY - minY)
    };
    return roi;
  }
}
