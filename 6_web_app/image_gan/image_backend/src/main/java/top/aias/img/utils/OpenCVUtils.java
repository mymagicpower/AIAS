package top.aias.img.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenCV Utils 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class OpenCVUtils {
    /**
     * canny算法，边缘检测
     *
     * @param src
     * @return
     */
    public static Mat canny(Mat src) {
        org.opencv.core.Mat mat = src.clone();
        Imgproc.Canny(src, mat, 100, 200);
        return mat;
    }

    /**
     * 画线
     *
     * @param mat
     * @param point1
     * @param point2
     */
    public static void line(Mat mat, Point point1, Point point2) {
        Imgproc.line(mat, point1, point2, new Scalar(255, 255, 255), 1);
    }

    /**
     * NDArray to opencv_core.Mat
     *
     * @param manager
     * @param srcPoints
     * @param dstPoints
     * @return
     */
    public static Mat toOpenCVMat(NDManager manager, NDArray srcPoints, NDArray dstPoints) {
        NDArray svdMat = SVDUtils.transformationFromPoints(manager, srcPoints, dstPoints);
        double[] doubleArray = svdMat.toDoubleArray();
        Mat newSvdMat = new Mat(2, 3, CvType.CV_64F);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                newSvdMat.put(i, j, doubleArray[i * 3 + j]);
            }
        }
        return newSvdMat;
    }

    /**
     * double[][] points array to Mat
     * @param points
     * @return
     */
    public static Mat toOpenCVMat(double[][] points) {
        Mat mat = new Mat(5, 2, CvType.CV_64F);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                mat.put(i, j, points[i * 5 + j]);
            }
        }
        return mat;
    }

    /**
     * 变换矩阵的逆矩阵
     *
     * @param src
     * @return
     */
    public static Mat invertAffineTransform(Mat src) {
        Mat dst = src.clone();
        Imgproc.invertAffineTransform(src, dst);
        return dst;
    }

    /**
     * Mat to BufferedImage
     *
     * @param mat
     * @return
     */
    public static BufferedImage mat2Image(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        byte[] data = new byte[width * height * (int) mat.elemSize()];
        Imgproc.cvtColor(mat, mat, 4);
        mat.get(0, 0, data);
        BufferedImage ret = new BufferedImage(width, height, 5);
        ret.getRaster().setDataElements(0, 0, width, height, data);
        return ret;
    }

    /**
     * BufferedImage to Mat
     *
     * @param img
     * @return
     */
    public static Mat image2Mat(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(height, width, CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }


    /**
     * 透视变换
     *
     * @param src
     * @param dst
     * @param warp_mat
     * @return
     */
    public static Mat warpPerspective(Mat src, Mat dst, Mat warp_mat) {
        Mat dstClone = dst.clone();
//        org.opencv.core.Mat mat = new org.opencv.core.Mat(dst.rows(), dst.cols(), CvType.CV_8UC3);
        Imgproc.warpPerspective(src, dstClone, warp_mat, dst.size());
        return dstClone;
    }

    /**
     * 透视变换
     *
     * @param src
     * @param srcPoints
     * @param dstPoints
     * @return
     */
    public static Mat perspectiveTransform(Mat src, Mat srcPoints, Mat dstPoints) {
        Mat dst = src.clone();
        Mat warp_mat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Imgproc.warpPerspective(src, dst, warp_mat, dst.size());
        warp_mat.release();

        return dst;
    }

    /**
     * 透视变换
     *
     * @param src
     * @param dst
     * @param srcPoints
     * @param dstPoints
     * @return
     */
    public static Mat perspectiveTransform(Mat src, Mat dst, Mat srcPoints, Mat dstPoints) {
        Mat dstClone = dst.clone();
        Mat warp_mat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Imgproc.warpPerspective(src, dstClone, warp_mat, dst.size());
        warp_mat.release();

        return dstClone;
    }

    /**
     * 画边框
     *
     * @param mat
     * @param squares
     * @param topK
     */
    public static void drawSquares(Mat mat, NDArray squares, int topK) {
        for (int i = 0; i < topK; i++) {
            float[] points = squares.get(i).toFloatArray();
            List<MatOfPoint> matOfPoints = new ArrayList<>();
            MatOfPoint matOfPoint = new MatOfPoint();
            matOfPoints.add(matOfPoint);
            List<org.opencv.core.Point> pointList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                org.opencv.core.Point pt = new org.opencv.core.Point(points[2 * j], points[2 * j + 1]);
                pointList.add(pt);
                Imgproc.circle(mat, pt, 10, new Scalar(0, 255, 255), -1);
                Imgproc.putText(mat, "" + j, pt, Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1);
            }
            matOfPoint.fromList(pointList);
            Imgproc.polylines(mat, matOfPoints, true, new Scalar(200, 200, 0), 5);
        }
    }
}
