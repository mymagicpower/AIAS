package me.aias.ocr.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import me.aias.ocr.model.Point;
import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public class PointUtils {

    public static ai.djl.modality.cv.output.Point getCenterPoint(List<Point> points) {
        double sumX = 0;
        double sumY = 0;

        for (Point point : points) {
            sumX = sumX + point.getX();
            sumY = sumY + point.getY();
        }

        ai.djl.modality.cv.output.Point centerPoint = new ai.djl.modality.cv.output.Point(sumX / 4, sumY / 4);
        return centerPoint;
    }

    public static Point transformPoint(NDManager manager, Mat mat, Point point) {
        double[][] pointsArray = new double[3][3];
        DoubleRawIndexer ldIdx = mat.createIndexer();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pointsArray[i][j] = ldIdx.get(i, j);
            }
        }
        NDArray ndPoints = manager.create(pointsArray);

        double[] vector = new double[3];
        vector[0] = point.getX();
        vector[1] = point.getY();
        vector[2] = 1f;
        NDArray vPoints = manager.create(vector);
        vPoints = vPoints.reshape(3, 1);
        NDArray result = ndPoints.matMul(vPoints);
        double[] dArray = result.toDoubleArray();
        if (dArray[2] != 0) {
            point.setX((int) (dArray[0] / dArray[2]));
            point.setY((int) (dArray[1] / dArray[2]));
        }

        return point;
    }

    public static List<Point> transformPoints(NDManager manager, Mat mat, List<Point> points) {
        int cols = mat.cols();
        int rows = mat.rows();
        double[][] pointsArray = new double[rows][cols];
        DoubleRawIndexer ldIdx = mat.createIndexer();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pointsArray[i][j] = ldIdx.get(i, j);
            }
        }
        NDArray ndPoints = manager.create(pointsArray);

        double[] vector = new double[3];
        for (int i = 0; i < points.size(); i++) {
            vector[0] = points.get(i).getX();
            vector[1] = points.get(i).getY();
            vector[2] = 1f;
            NDArray vPoints = manager.create(vector);
            vPoints = vPoints.reshape(3, 1);
            NDArray result = ndPoints.matMul(vPoints);
            double[] dArray = result.toDoubleArray();
            if (dArray.length > 2) {
                if (dArray[2] != 0) {
                    points.get(i).setX((int) (dArray[0] / dArray[2]));
                    points.get(i).setY((int) (dArray[1] / dArray[2]));
                }
            } else {
                points.get(i).setX((int) (dArray[0]));
                points.get(i).setY((int) (dArray[1]));
            }

        }

        return points;
    }

    /**
     * Get (x1,y1,x2,y2) coordinations
     *
     * @param points
     * @return
     */
    public static int[] rectXYXY(List<Point> points) {
        int left = points.get(0).getX();
        int top = points.get(0).getY();
        int right = points.get(2).getX();
        int bottom = points.get(2).getY();
        return new int[]{left, top, right, bottom};
    }

    /**
     * Get (x1,y1,w,h) coordinations
     *
     * @param points
     * @return
     */
    public static int[] rectXYWH(List<Point> points) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            int x = point.getX();
            int y = point.getY();
            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
        }

        int w = maxX - minX;
        int h = maxY - minY;
        return new int[]{minX, minY, w, h};
    }
}
