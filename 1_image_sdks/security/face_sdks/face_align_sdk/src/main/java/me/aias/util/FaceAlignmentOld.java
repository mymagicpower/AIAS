package me.aias.util;

import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.CvMat;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.Point2fVector;

import java.nio.FloatBuffer;

import static org.bytedeco.opencv.global.opencv_calib3d.findHomography;
import static org.bytedeco.opencv.global.opencv_core.cvCreateMat;

/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public class FaceAlignmentOld {

  public static Mat affineTransform(
          Mat src, Point2f srcPoints, Point2f dstPoints) {
    Mat dst = src.clone();
    // https://github.com/bytedeco/javacv/issues/788
    Mat warp_mat = opencv_imgproc.getAffineTransform(srcPoints.position(0), dstPoints.position(0));
    opencv_imgproc.warpAffine(src, dst, warp_mat, dst.size());
    return dst;
  }

  public static Mat perspectiveTransform(
          Mat src, Point2f srcPoints, Point2f dstPoints) {
    Mat dst = src.clone();
    Mat warp_mat = opencv_imgproc.getPerspectiveTransform(srcPoints.position(0), dstPoints.position(0));
    opencv_imgproc.warpPerspective(src, dst, warp_mat, dst.size());
    return dst;
  }

  public static Mat getHomography(Mat src, double[] srcPoints, double[] dstPoints) {
    Mat dst = src.clone();
    Mat warp_mat = createHomography(srcPoints, dstPoints);
    opencv_imgproc.warpPerspective(src, dst, warp_mat, dst.size());
    return dst;
  }

  public static Mat createHomography(double[] src, double[] dst) {
    CvMat srcPoints;
    CvMat dstPoints;
    int nbPoints = src.length / 2;
    Mat homography;

    srcPoints = cvCreateMat(2, nbPoints, opencv_core.CV_32FC1);
    dstPoints = cvCreateMat(2, nbPoints, opencv_core.CV_32FC1);
    Mat newSvdMat = new Mat(2, 3, opencv_core.CV_32FC1);

//    homography = cvCreateMat(3, 3, opencv_core.CV_32FC1);

    for (int i = 0; i < nbPoints; i++) {
      srcPoints.put(i, src[2 * i]);
      srcPoints.put(i + nbPoints, src[2 * i + 1]);
      dstPoints.put(i, dst[2 * i]);
      dstPoints.put(i + nbPoints, dst[2 * i + 1]);
    }
    homography = findHomography(returnMat(srcPoints), returnMat(dstPoints));
    return homography;
  }

  public static Mat returnMat(CvMat mtx) {
    double valor;
    final int rows = mtx.rows();
    final int cols = mtx.cols();
    Mat mat = new Mat(rows, cols, opencv_core.CV_64F);
    final int step = mtx.step() / 4;
    FloatBuffer buf = mtx.getFloatBuffer();
    DoubleRawIndexer ldIdx = mat.createIndexer();
    for (int row = 0; row < rows; row++) {
      buf.position(row * step);
      for (int col = 0; col < cols; col++) {
        valor = buf.get();
        ldIdx.put(row, col, valor);
      }
    }
    ldIdx.release();
    return mat;
  }

  // 根据目标5点，进行旋转仿射变换
  // Perform rotation affine transformation based on the target 5 points
  public static Mat get5WarpAffineImg(Mat src, Mat rot_mat) {
    Mat oral = new Mat();
    src.copyTo(oral);
    Mat rot = new Mat();
    // 进行仿射变换，变换后大小为src的大小
    // Perform affine transformation, and the size after transformation is the same as that of src
    opencv_imgproc.warpAffine(src, rot, rot_mat, src.size());
    return rot;
  }

  // 根据两眼中心点，进行旋转仿射变换
  // Perform rotation affine transformation based on the center point of two eyes
  //      Point2fVector pv = FaceUtils.point2fVector(subImageRect, points);
  //      mat = FaceAlignment.get5WarpAffineImg(mat, pv);
  public static Mat get5WarpAffineImg(Mat src, Point2fVector landmarks) {
    Mat oral = new Mat();
    src.copyTo(oral);
    //	  图中关键点坐标
    //    Coordinates of keypoints in the image
    //	  1.  left_eye_x , left_eye_y
    //	  2.  right_eye_x , right_eye_y
    //	  3.  nose_x , nose_y
    //	  4.  left_mouth_x , left_mouth_y
    //	  5.  right_mouth_x , right_mouth_y

    // 计算两眼中心点，按照此中心点进行旋转， 第1个为左眼坐标，第2个为右眼坐标
    // Calculate the center point of two eyes and rotate according to this center point, where the first is the coordinate of the left eye and the second is the coordinate of the right eye
    Point2f eyesCenter = new Point2f(landmarks.get()[2].x(), landmarks.get()[2].y()); // 第 3 个点为两眼之间 - The third point is in the middle of the two eyes

    // 计算两个眼睛间的角度
    // Calculate the angle between the two eyes
    float dy = (landmarks.get()[1].y() - landmarks.get()[0].y()); // 2 - 1
    float dx = (landmarks.get()[1].x() - landmarks.get()[0].x()); // 2 - 1

    double angle = Math.atan2(dy, dx) * 180.0 / opencv_core.CV_PI;
    // 弧度转角度
    // Convert radians to degrees
    // 由 eyesCenter, angle, scale 按照公式计算仿射变换矩阵，此时1.0表示不进行缩放
    // Calculate the affine transformation matrix according to the formula based on eyesCenter, angle and scale, where 1.0 means no scaling
    // cv2.getRotationMatrix2D 三个参数分别为：1.旋转中心，2.旋转角度，3.缩放比例。角度为正，则图像逆时针旋转，旋转后图像可能会超出边界。
    // cv2.getRotationMatrix2D has three parameters: 1. Rotation center, 2. Rotation angle, 3. Scaling ratio. If the angle is positive, the image is rotated counterclockwise, and the rotated image may exceed the boundary.
    Mat rot_mat = opencv_imgproc.getRotationMatrix2D(eyesCenter, angle, 1.0);
    Mat rot = new Mat();
    // 进行仿射变换，变换后大小为src的大小
    // Perform affine transformation, and the size after transformation is the same as that of src
    opencv_imgproc.warpAffine(src, rot, rot_mat, src.size());
    return rot;
  }

  public static Mat get68WarpAffineImg(Mat src, Point2fVector landmarks) {
    Mat oral = new Mat();
    src.copyTo(oral);
    //		for (int j = 0; j < landmarks.get().length; j++)     {
    //			opencv_imgproc.circle(oral, new Point((int) landmarks.get()[j].x(),(int)
    // landmarks.get()[j].y()),2,new Scalar(255, 0, 0 ,0));
    //		}
    //		opencv_imgcodecs.imwrite("/Users/calvin/Documents/Data_Faces_0/fa_result_1.jpg",oral);

    // 计算两眼中心点，按照此中心点进行旋转， 第40个点为左眼坐标，第43个点为右眼坐标
    // Calculate the center point of two eyes and rotate according to this center point, where the 40th point is the coordinate of the left eye and the 43rd point is the coordinate of the right eye
    // Point2f eyesCenter = new Point2f( (landmarks.get()[39].x() + landmarks.get()[42].x()) * 0.5f,
    // (landmarks.get()[39].y() + landmarks.get()[42].y()) * 0.5f );
    Point2f eyesCenter =
            new Point2f(landmarks.get()[27].x(), landmarks.get()[27].y()); // 第 28 个点为两眼之间

    // 计算两个眼睛间的角度
    // Calculate the angle between the two eyes
    float dy = (landmarks.get()[42].y() - landmarks.get()[39].y()); // 43 - 40
    float dx = (landmarks.get()[42].x() - landmarks.get()[39].x()); // 43 - 40

    double angle = Math.atan2(dy, dx) * 180.0 / opencv_core.CV_PI;
    // 弧度转角度
    // Convert radians to degrees
    // 由eyesCenter, angle, scale 按照公式计算仿射变换矩阵，此时1.0表示不进行缩放
    // Calculate the affine transformation matrix based on eyesCenter, angle and scale, where 1.0 means no scaling
    Mat rot_mat = opencv_imgproc.getRotationMatrix2D(eyesCenter, angle, 1.0);
    Mat rot = new Mat();
    // 进行仿射变换，变换后大小为src的大小
    // Perform affine transformation, and the size after transformation is the same as that of src
    opencv_imgproc.warpAffine(src, rot, rot_mat, src.size());
    return rot;
  }
}
