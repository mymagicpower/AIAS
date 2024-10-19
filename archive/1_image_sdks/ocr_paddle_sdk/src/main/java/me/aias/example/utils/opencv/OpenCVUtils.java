package me.aias.example.utils.opencv;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;


public class OpenCVUtils {

    public static Mat perspectiveTransform(
            Mat src, Point2f srcPoints, Point2f dstPoints) {
        Mat dst = src.clone();
        Mat warp_mat = opencv_imgproc.getPerspectiveTransform(srcPoints.position(0), dstPoints.position(0));
        opencv_imgproc.warpPerspective(src, dst, warp_mat, dst.size());
        warp_mat.release();
        warp_mat.close();

        return dst;
    }

    /**
     * Mat to BufferedImage
     *
     * @param mat
     * @return
     */
    public static BufferedImage mat2Image(org.opencv.core.Mat mat) {
        int width = mat.width();
        int height = mat.height();
        byte[] data = new byte[width * height * (int) mat.elemSize()];
        Imgproc.cvtColor(mat, mat, 4);
        mat.get(0, 0, data);
        BufferedImage ret = new BufferedImage(width, height, 5);
        ret.getRaster().setDataElements(0, 0, width, height, data);
        return ret;
    }

    public static BufferedImage matToBufferedImage(org.opencv.core.Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    /**
     * BufferedImage to Mat
     *
     * @param img
     * @return
     */
    public static org.opencv.core.Mat image2Mat(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        org.opencv.core.Mat mat = new org.opencv.core.Mat(height, width, CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }
}
