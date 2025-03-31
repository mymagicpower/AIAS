package top.aias.utils;

import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * OpenCV Utils 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class OpenCVUtils {
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
            List<Point> pointList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Point pt = new Point(points[2 * j], points[2 * j + 1]);
                pointList.add(pt);
                Imgproc.circle(mat, pt, 10, new Scalar(0, 255, 255), -1);
                Imgproc.putText(mat, "" + j, pt, Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1);
            }
            matOfPoint.fromList(pointList);
            Imgproc.polylines(mat, matOfPoints, true, new Scalar(200, 200, 0), 5);
        }
    }

    /**
     * 调整图片大小
     * @param srcMat
     * @param width
     * @param height
     * @return
     */
    public static Mat resize(Mat srcMat, int width, int height, int interpolation) {
        Mat dstMat = srcMat.clone();
        Imgproc.resize(srcMat, dstMat,new Size(width, height),1.0f,1.0f,interpolation);
        return dstMat;
    }

    /**
     * 生成随机高对比度颜色
     * @return
     */
    public static Scalar getColor() {
        // 色相值（0-360）
        float hue = ThreadLocalRandom.current().nextFloat() * 360;
        float saturation = 0.9f; // 设置高饱和度
        float value = 0.9f;      // 设置高亮度

        // HSV 转 RGB
        return hsvToBgr(hue, saturation, value);
    }

    /**
     * HSV 转 RGB
     * @param hue
     * @param saturation
     * @param value
     * @return
     */
    public static Scalar hsvToBgr(float hue, float saturation, float value) {
        int h = (int) (hue / 60) % 6;
        float f = (hue / 60) - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        float r = 0, g = 0, b = 0;
        switch (h) {
            case 0: {
                r = value;
                g = t;
                b = p;
            }
            break;
            case 1: {
                r = q;
                g = value;
                b = p;
            }
            break;
            case 2: {
                r = p;
                g = value;
                b = t;
            }
            break;
            case 3: {
                r = p;
                g = q;
                b = value;
            }
            break;
            case 4: {
                r = t;
                g = p;
                b = value;
            }
            break;
            case 5: {
                r = value;
                g = p;
                b = q;
            }
        }

        return new Scalar(b * 255, g * 255, r * 255, 1);
    }
}
