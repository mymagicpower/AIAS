package me.aias.example.opencv;

import ai.djl.ndarray.NDArray;
import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

public class NDArrayUtils {

    public static MatOfPoint matToMatOfPoint(Mat mat) {
        int rows = mat.rows();
        MatOfPoint matOfPoint = new MatOfPoint();

        List<Point> list = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            Point point = new Point((float) mat.get(i, 0)[0], (float) mat.get(i, 1)[0]);
            list.add(point);
        }
        matOfPoint.fromList(list);

        return matOfPoint;
    }


    public static Mat floatNDArrayToMat(NDArray ndArray) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        Mat mat = new Mat(rows, cols, CvType.CV_32F);

        float[] arrs = ndArray.toFloatArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arrs[i * cols + j]);
            }
        }
        return mat;
    }

    public static Mat floatNDArrayToMat(NDArray ndArray, int cvType) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        Mat mat = new Mat(rows, cols, cvType);

        float[] arrs = ndArray.toFloatArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arrs[i * cols + j]);
            }
        }
        return mat;
    }

    public static Mat uint8NDArrayToMat(NDArray ndArray) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        Mat mat = new Mat(rows, cols, CvType.CV_8U);

        byte[] arrs = ndArray.toByteArray();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arrs[i * cols + j]);
            }
        }
        return mat;
    }

    public static org.opencv.core.Mat uint8ArrayToMat(byte[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;
        org.opencv.core.Mat mat = new Mat(rows, cols, CvType.CV_8U);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arr[i][j]);
            }
        }

        return mat;
    }

    // list 转 Mat
    public static Mat toMat(List<ai.djl.modality.cv.output.Point> points) {
        Mat mat = new Mat(points.size(), 2, CvType.CV_32F);
        for (int i = 0; i < points.size(); i++) {
            ai.djl.modality.cv.output.Point point = points.get(i);
            mat.put(i, 0, (float) point.getX());
            mat.put(i, 1, (float) point.getY());
        }

        return mat;
    }
}
