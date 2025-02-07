package top.aias.facesr.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * OpenCV 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class OpenCVUtils {
    public static NDArray cvtColor(NDArray oriArray, int code) {
        long[] oriShape = oriArray.getShape().getShape();
        Mat src = new Mat((int) oriShape[0], (int) oriShape[1], CvType.CV_32FC3);
        float[] dataArr = oriArray.toFloatArray();
        src.put(0, 0, dataArr);
        Mat dst = src.clone();
        Imgproc.cvtColor(src, dst, code);

        NDArray ndArray = OpenCVUtils.mat2NDArray(oriArray.getManager(), dst);

        // 释放 mat - opencv 资源要记得主动释放，否则内存泄漏
        src.release();
        dst.release();

        return ndArray;
    }

    public static NDArray mat2NDArray(NDManager manager, Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();
        int channels = mat.channels();
        NDArray ndArray = manager.create(new Shape(new long[]{rows, cols, channels}), DataType.FLOAT32);
        // 将Mat对象的数据复制到NDArray对象中
        float[] data = new float[rows * cols * channels];
        mat.get(0, 0, data);
        ndArray.set(data);

        return ndArray;
    }
}
