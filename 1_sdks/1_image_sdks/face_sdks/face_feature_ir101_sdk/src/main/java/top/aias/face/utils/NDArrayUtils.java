package top.aias.face.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

/**
 * NDArray 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class NDArrayUtils {
    // NDArray to opencv_core.Mat
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

    public static MatOfPoint2f convert(NDArray ndArray) {
        // 获取NDArray的形状和数据类型
        Shape shape = ndArray.getShape();
        DataType dataType = ndArray.getDataType();

        // 检查形状是否为 [n, 2]，数据类型是否为 float32
        if (shape.dimension() != 2 || shape.get(1) != 2 || dataType != DataType.FLOAT32) {
            throw new IllegalArgumentException("NDArray shape should be [n, 2] and data type should be float32");
        }

        // 获取NDArray的数据
        float[] data = ndArray.toFloatArray();

        // 创建OpenCV的MatOfPoint2f对象
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();

        // 将NDArray的数据转换为OpenCV的Point对象数组
        Point[] points = new Point[(int) shape.get(0)];
        for (int i = 0; i < shape.get(0); i++) {
            int index = i * 2;
            points[i] = new Point(data[index], data[index + 1]);
        }

        // 使用MatOfPoint2f的fromArray方法将Point对象数组添加到MatOfPoint2f中
        matOfPoint2f.fromArray(points);

        return matOfPoint2f;
    }
}
