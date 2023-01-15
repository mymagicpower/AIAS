package me.aias.ocr.utils;

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


public class OpenCVUtils {

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
}
