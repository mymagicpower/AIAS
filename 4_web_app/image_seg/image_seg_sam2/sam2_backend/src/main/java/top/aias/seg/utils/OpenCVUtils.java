package top.aias.seg.utils;

import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.opencv.core.*;
import top.aias.seg.bean.Point;
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
     * 高斯滤波器(GaussianFilter)对图像进行平滑处理
     * @param manager
     * @param ndArray
     * @return
     */
    public static NDArray gaussianBlur(NDManager manager, NDArray ndArray) {
        org.opencv.core.Mat src = NDArrayUtils.floatNDArrayToMat(ndArray);
        org.opencv.core.Mat morphMat = src.clone();
        org.opencv.core.Mat gaussMat = src.clone();
        org.opencv.core.Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));

        // 形态学操作函数: 它可以对图像进行膨胀、腐蚀、开运算、闭运算等操作,从而得到更好的效果。
        Imgproc.morphologyEx(src, morphMat, Imgproc.MORPH_OPEN, kernel);
        // 高斯滤波器(GaussianFilter)对图像进行平滑处理
        Imgproc.GaussianBlur(morphMat, gaussMat, new Size(5, 5), 2.0f, 2.0f);
        float[][] gaussArr = NDArrayUtils.matToFloatArray(gaussMat);
        NDArray gaussNDArray = manager.create(gaussArr);

        // release mat
        src.release();
        morphMat.release();
        gaussMat.release();
        kernel.release();

        return gaussNDArray;
    }
    /**
     * Draws a rectangle on the image.
     *
     * @param rectangle the rectangle to draw
     * @param rgb the color
     * @param stroke the thickness
     */
    public static void drawRectangle(Mat image, Rectangle rectangle, int rgb, int stroke) {
        Rect rect =
                new Rect(
                        (int) rectangle.getX(),
                        (int) rectangle.getY(),
                        (int) rectangle.getWidth(),
                        (int) rectangle.getHeight());
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0x00ff00) >> 8;
        int b = rgb & 0x0000ff;
        Scalar color = new Scalar(b, g, r);
        Imgproc.rectangle(image, rect.tl(), rect.br(), color, stroke);
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
     * list 转 Mat
     *
     * @param points
     * @return
     */
    public static Mat toMat(List<Point> points) {
        Mat mat = new Mat(points.size(), 2, CvType.CV_32F);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            mat.put(i, 0, (float) point.getX());
            mat.put(i, 1, (float) point.getY());
        }

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
     * 图片裁剪
     *
     * @param points
     * @return
     */
    public static int[] imgCrop(float[] points) {
        int[] wh = new int[2];
        float[] lt = java.util.Arrays.copyOfRange(points, 0, 2);
        float[] rt = java.util.Arrays.copyOfRange(points, 2, 4);
        float[] rb = java.util.Arrays.copyOfRange(points, 4, 6);
        float[] lb = java.util.Arrays.copyOfRange(points, 6, 8);
        wh[0] = (int) Math.max(PointUtils.distance(lt, rt), PointUtils.distance(rb, lb));
        wh[1] = (int) Math.max(PointUtils.distance(lt, lb), PointUtils.distance(rt, rb));
        return wh;
    }

    /**
     * 转正图片
     *
     * @param mat
     * @param points
     * @return
     */
    public static Mat perspectiveTransform(Mat mat, float[] points) {
        float[] lt = java.util.Arrays.copyOfRange(points, 0, 2);
        float[] rt = java.util.Arrays.copyOfRange(points, 2, 4);
        float[] rb = java.util.Arrays.copyOfRange(points, 4, 6);
        float[] lb = java.util.Arrays.copyOfRange(points, 6, 8);
        int img_crop_width = (int) Math.max(PointUtils.distance(lt, rt), PointUtils.distance(rb, lb));
        int img_crop_height = (int) Math.max(PointUtils.distance(lt, lb), PointUtils.distance(rt, rb));
        List<Point> srcPoints = new ArrayList<>();
        srcPoints.add(new Point((int)lt[0], (int)lt[1]));
        srcPoints.add(new Point((int)rt[0], (int)rt[1]));
        srcPoints.add(new Point((int)rb[0], (int)rb[1]));
        srcPoints.add(new Point((int)lb[0], (int)lb[1]));
        List<Point> dstPoints = new ArrayList<>();
        dstPoints.add(new Point(0, 0));
        dstPoints.add(new Point(img_crop_width, 0));
        dstPoints.add(new Point(img_crop_width, img_crop_height));
        dstPoints.add(new Point(0, img_crop_height));

        Mat srcPoint2f = toMat(srcPoints);
        Mat dstPoint2f = toMat(dstPoints);

        Mat cvMat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);
        srcPoint2f.release();
        dstPoint2f.release();
        return cvMat;
    }
    /**
     * 转正图片 - 废弃
     *
     * @param mat
     * @param points
     * @return
     */
    public Mat perspectiveTransformOld(Mat mat, float[] points) {
        List<org.opencv.core.Point> pointList = new ArrayList<>();
        float[][] srcArr = new float[4][2];
        float min_X = Float.MAX_VALUE;
        float min_Y = Float.MAX_VALUE;
        float max_X = -1;
        float max_Y = -1;

        for (int j = 0; j < 4; j++) {
            org.opencv.core.Point pt = new org.opencv.core.Point(points[2 * j], points[2 * j + 1]);
            pointList.add(pt);
            srcArr[j][0] = points[2 * j];
            srcArr[j][1] = points[2 * j + 1];
            if (points[2 * j] > max_X) {
                max_X = points[2 * j];
            }
            if (points[2 * j] < min_X) {
                min_X = points[2 * j];
            }
            if (points[2 * j + 1] > max_Y) {
                max_Y = points[2 * j + 1];
            }
            if (points[2 * j + 1] < min_Y) {
                min_Y = points[2 * j + 1];
            }
        }

        Mat src = NDArrayUtils.floatArrayToMat(srcArr);

        float width = max_Y - min_Y;
        float height = max_X - min_X;

        float[][] dstArr = new float[4][2];
        dstArr[0] = new float[]{0, 0};
        dstArr[1] = new float[]{width - 1, 0};
        dstArr[2] = new float[]{width - 1, height - 1};
        dstArr[3] = new float[]{0, height - 1};

        Mat dst = NDArrayUtils.floatArrayToMat(dstArr);
        return OpenCVUtils.perspectiveTransform(mat, src, dst);
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
