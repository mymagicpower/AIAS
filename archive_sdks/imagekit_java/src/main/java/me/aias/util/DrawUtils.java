package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;

import static org.bytedeco.opencv.global.opencv_imgproc.minAreaRect;

/** 画图工具类 */
public class DrawUtils {
  /**
   * 画出所有的矩形
   *
   * @param src
   * @return
   */
  public static Mat drawContours(Mat src, String filePath) {
    Mat cannyMat = GeneralUtils.canny(src);
    MatVector contours = ContourUtils.getContours(cannyMat);

    Mat rectMat = src.clone();
    Scalar scalar = new Scalar(255, 0, 0, 1);

    for (long i = contours.size() - 1; i >= 0; i--) {
      Mat mat = contours.get(i);
      RotatedRect rect = minAreaRect(mat);

      Rect r = rect.boundingRect();

      System.out.println(r.area() + " --- " + i);

      rectMat = drawRect(rectMat, r, scalar);
    }

    GeneralUtils.saveImg(rectMat, filePath);
    return rectMat;
  }

  /**
   * 画出最大的矩形
   *
   * @param src
   * @return
   */
  public static void drawMaxRect(Mat src, String filePath) {
    Mat cannyMat = GeneralUtils.canny(src);

    RotatedRect rect = RectUtils.getMaxRect(cannyMat);

    Rect r = rect.boundingRect();

    Mat rectMat = src.clone();
    Scalar scalar = new Scalar(255, 0, 0, 1);

    rectMat = drawRect(rectMat, r, scalar);

    GeneralUtils.saveImg(rectMat, filePath);
  }

  /**
   * 画矩形
   *
   * @param src
   * @param r
   * @param scalar
   * @return
   */
  public static Mat drawRect(Mat src, Rect r, Scalar scalar) {
    Point pt1 = new Point(r.x(), r.y());
    Point pt2 = new Point(r.x() + r.width(), r.y());
    Point pt3 = new Point(r.x() + r.width(), r.y() + r.height());
    Point pt4 = new Point(r.x(), r.y() + r.height());

    opencv_imgproc.line(src, pt1, pt2, scalar);
    opencv_imgproc.line(src, pt2, pt3, scalar);
    opencv_imgproc.line(src, pt3, pt4, scalar);
    opencv_imgproc.line(src, pt4, pt1, scalar);

    return src;
  }

  /**
   * 画实心圆
   *
   * @param src
   * @param point 点
   * @param size 点的尺寸
   * @param scalar 颜色
   * @param path 保存路径
   */
  public static boolean drawCircle(Mat src, Point[] point, int size, Scalar scalar, String path) {
    if (src == null || point == null) {
      throw new RuntimeException("Mat 或者 Point 数组不能为NULL");
    }
    for (Point p : point) {
      opencv_imgproc.circle(src, p, size, scalar);
    }

    if (path != null && !"".equals(path)) {
      return GeneralUtils.saveImg(src, path);
    }

    return false;
  }
}
