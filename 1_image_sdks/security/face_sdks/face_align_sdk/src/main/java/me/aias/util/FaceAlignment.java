package me.aias.util;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public class FaceAlignment {
  // 根据目标5点，进行旋转仿射变换
  // Perform rotation and affine transformation based on the target 5 points
  public static Mat get5WarpAffineImg(Mat src, Mat rot_mat) {
    Mat oral = new Mat();
    src.copyTo(oral);
    Mat rot = new Mat();
    // 进行仿射变换，变换后大小为src的大小
    // Perform affine transformation, the size after transformation is the same as the size of src
    Imgproc.warpAffine(src, rot, rot_mat, src.size());
    return rot;
  }
}
