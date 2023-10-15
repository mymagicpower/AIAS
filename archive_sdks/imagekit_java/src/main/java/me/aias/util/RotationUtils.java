package me.aias.util;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.RotatedRect;
import org.bytedeco.opencv.opencv_core.Scalar;

import static org.bytedeco.opencv.global.opencv_imgproc.getRotationMatrix2D;
import static org.bytedeco.opencv.global.opencv_imgproc.warpAffine;

/**
 * 旋转矩形工具类
 * Rotation Rectangle Tool Class
 */
public class RotationUtils {

  /**
   * 旋转矩形 返回旋转后的Mat
   * Rotate rectangle and return rotated Mat
   * @param cannyMat mat矩阵 - matrix
   * @param rect 矩形 - rectangle
   * @return
   */
  public static Mat rotation(Mat cannyMat, RotatedRect rect) {
    double angle = rect.angle();
    Point2f center = rect.center();
    Mat correctImg = new Mat(cannyMat.size(), cannyMat.type());
    cannyMat.copyTo(correctImg);

    // 得到旋转矩阵算子 - Get the rotation matrix operator
    Mat matrix = getRotationMatrix2D(center, angle, 0.8);

    warpAffine(correctImg, correctImg, matrix, correctImg.size(), 1, 0, new Scalar(0, 0, 0, 0));

    return correctImg;
  }
}
