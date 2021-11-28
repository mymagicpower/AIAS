package me.aias.util;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.opencv.imgproc.Imgproc;

/** 透视变换工具类 因为我透视变换做的也不是很好，就仅提供一个大概的函数... */
public class WarpPerspectiveUtils {

  /**
   * 透视变换
   *
   * @param src
   * @param srcPoints
   * @param dstPoints
   * @return
   */
  public static Mat warpPerspective(Mat src, Mat srcPoints, Mat dstPoints) {
    // srcPoints, dstPoints
    // 点的顺序[左上 ，右上 ，右下 ，左下]
    Mat perspectiveMmat = opencv_imgproc.getPerspectiveTransform(srcPoints, dstPoints);

    Mat dst = new Mat();

    opencv_imgproc.warpPerspective(
        src,
        dst,
        perspectiveMmat,
        src.size(),
        Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP,
        1,
        new Scalar(0));

    return dst;
  }

  public static Mat perspectiveTransform(
          Mat src, Point2f srcPoints, Point2f dstPoints) {
    Mat dst = src.clone();
    Mat warp_mat = opencv_imgproc.getPerspectiveTransform(srcPoints.position(0), dstPoints.position(0));
    opencv_imgproc.warpPerspective(src, dst, warp_mat, dst.size());
    return dst;
  }
}
