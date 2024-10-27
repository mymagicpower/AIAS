package me.aias.example.utils.common;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;

import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public class PointUtils {
    /**
     * 计算两点距离
     * @param point1
     * @param point2
     * @return
     */
    public static float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    /**
     * 计算两点距离
     * @param point1
     * @param point2
     * @return
     */
    public static float distance(Point point1, Point point2) {
        double disX = point1.getX() - point2.getX();
        double disY = point1.getY() - point2.getY();
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    /**
     * sort the points based on their x-coordinates
     * 顺时针
     *
     * @param pts
     * @return
     */

    private static NDArray order_points_clockwise(NDArray pts) {
        NDList list = new NDList();
        long[] indexes = pts.get(":, 0").argSort().toLongArray();

        // grab the left-most and right-most points from the sorted
        // x-roodinate points
        Shape s1 = pts.getShape();
        NDArray leftMost1 = pts.get(indexes[0] + ",:");
        NDArray leftMost2 = pts.get(indexes[1] + ",:");
        NDArray leftMost = leftMost1.concat(leftMost2).reshape(2, 2);
        NDArray rightMost1 = pts.get(indexes[2] + ",:");
        NDArray rightMost2 = pts.get(indexes[3] + ",:");
        NDArray rightMost = rightMost1.concat(rightMost2).reshape(2, 2);

        // now, sort the left-most coordinates according to their
        // y-coordinates so we can grab the top-left and bottom-left
        // points, respectively
        indexes = leftMost.get(":, 1").argSort().toLongArray();
        NDArray lt = leftMost.get(indexes[0] + ",:");
        NDArray lb = leftMost.get(indexes[1] + ",:");
        indexes = rightMost.get(":, 1").argSort().toLongArray();
        NDArray rt = rightMost.get(indexes[0] + ",:");
        NDArray rb = rightMost.get(indexes[1] + ",:");

        list.add(lt);
        list.add(rt);
        list.add(rb);
        list.add(lb);

        NDArray rect = NDArrays.concat(list).reshape(4, 2);
        return rect;
    }

    /**
     * 计算四边形的面积
     * 根据海伦公式（Heron's formula）计算面积
     *
     * @param arr
     * @return
     */
    public static double getQuadArea(NDManager manager, double[][] arr) {
        NDArray ndArray = manager.create(arr).reshape(4, 2);
        ndArray = order_points_clockwise(ndArray);
        double[] array = ndArray.toDoubleArray();

        double x1 = array[0];
        double y1 = array[1];
        double x2 = array[2];
        double y2 = array[3];
        double x3 = array[4];
        double y3 = array[5];
        double x4 = array[6];
        double y4 = array[7];

        double totalArea;
        if (isInTriangle(x2, y2, x3, y3, x4, y4, x1, y1)) { // 判断点 (x1, y1) 是否在三角形 (x2,y2)，(x3,y3)，(x4,y4) 内
            double area1 = getTriangleArea(x2, y2, x3, y3, x1, y1);
            double area2 = getTriangleArea(x2, y2, x4, y4, x1, y1);
            double area3 = getTriangleArea(x3, y3, x4, y4, x1, y1);
            totalArea = area1 + area2 + area3;
        } else if (isInTriangle(x1, y1, x3, y3, x4, y4, x2, y2)) {// 判断点 (x2, y2) 是否在三角形 (x1,y1)，(x3,y3)，(x4,y4) 内
            double area1 = getTriangleArea(x1, y1, x3, y3, x2, y2);
            double area2 = getTriangleArea(x1, y1, x4, y4, x2, y2);
            double area3 = getTriangleArea(x3, y3, x4, y4, x2, y2);
            totalArea = area1 + area2 + area3;
        } else if (isInTriangle(x1, y1, x2, y2, x4, y4, x3, y3)) {// 判断点 (x3, y3) 是否在三角形 (x1,y1)，(x2,y2)，(x4,y4) 内
            double area1 = getTriangleArea(x1, y1, x2, y2, x3, y3);
            double area2 = getTriangleArea(x1, y1, x4, y4, x3, y3);
            double area3 = getTriangleArea(x2, y2, x4, y4, x3, y3);
            totalArea = area1 + area2 + area3;
        } else if (isInTriangle(x1, y1, x2, y2, x3, y3, x4, y4)) {// 判断点 (x4, y4) 是否在三角形 (x1,y1)，(x2,y2)，(x3,y3) 内
            double area1 = getTriangleArea(x1, y1, x2, y2, x4, y4);
            double area2 = getTriangleArea(x1, y1, x3, y3, x4, y4);
            double area3 = getTriangleArea(x2, y2, x3, y3, x4, y4);
            totalArea = area1 + area2 + area3;
        } else {
            double area1 = getTriangleArea(x1, y1, x2, y2, x3, y3);
            double area2 = getTriangleArea(x1, y1, x3, y3, x4, y4);
            totalArea = area1 + area2;
        }

        return totalArea;
    }

    /**
     * 判断点 (px, py) 是否在三角形 (x1,y1)，(x2,y2)，(x3,y3) 内
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param px
     * @param py
     * @return
     */
    public static boolean isInTriangle(double x1, double y1, double x2, double y2, double x3, double y3, double px, double py) {
        if(!isTriangle(x1, y1, x2, y2, px, py))
            return false;
        double area1 = getTriangleArea(x1, y1, x2, y2, px, py);
        if(!isTriangle(x1, y1, x3, y3, px, py))
            return false;
        double area2 = getTriangleArea(x1, y1, x3, y3, px, py);
        if(!isTriangle(x2, y2, x3, y3, px, py))
            return false;
        double area3 = getTriangleArea(x2, y2, x3, y3, px, py);
        if(!isTriangle(x1, y1, x2, y2, x3, y3))
            return false;
        double totalArea = getTriangleArea(x1, y1, x2, y2, x3, y3);
        double delta = Math.abs(totalArea - (area1 + area2 + area3));
        if (delta < 1)
            return true;
        else
            return false;
    }

    /**
     * 给定3个点坐标(x1,y1)，(x2,y2)，(x3,y3)，给出判断是否能组成三角形
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    public static boolean isTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
        double b = Math.sqrt(Math.pow(x1-x3, 2) + Math.pow(y1-y3, 2));
        double c = Math.sqrt(Math.pow(x2-x3, 2) + Math.pow(y2-y3, 2));
        return a + b > c && b + c > a && a + c > b;
    }

    /**
     * 计算三角形的面积
     * 根据海伦公式（Heron's formula）计算三角形面积
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    public static double getTriangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double b = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double c = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));
        double p = (a + b + c) / 2;
        double area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        return area;
    }

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

    public static Point transformPoint(NDManager manager, org.opencv.core.Mat mat, Point point) {
        double[][] pointsArray = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pointsArray[i][j] = mat.get(i, j)[0];
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

    public static List<Point> transformPoints(NDManager manager, org.opencv.core.Mat mat, List<Point> points) {
        int cols = mat.cols();
        int rows = mat.rows();
        double[][] pointsArray = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pointsArray[i][j] = mat.get(i, j)[0];
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
