package top.aias.img.utils;

import ai.djl.ndarray.NDArray;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
/**
 * NDArray 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class NDArrayUtils {
    public static double[][] matToDoubleArray(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();

        double[][] doubles = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                doubles[i][j] = mat.get(i, j)[0];
            }
        }

        return doubles;
    }

    public static float[][] matToFloatArray(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();

        float[][] floats = new float[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                floats[i][j] = (float) mat.get(i, j)[0];
            }
        }

        return floats;
    }

    public static byte[][] matToUint8Array(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();

        byte[][] bytes = new byte[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bytes[i][j] = (byte) mat.get(i, j)[0];
            }
        }

        return bytes;
    }

    public static int[][] intNDArrayToArray(NDArray ndArray) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        int[][] arr = new int[rows][cols];

        int[] arrs = ndArray.toIntArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = arrs[i * cols + j];
            }
        }
        return arr;
    }

    public static float[][] floatNDArrayToArray(NDArray ndArray) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        float[][] arr = new float[rows][cols];

        float[] arrs = ndArray.toFloatArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = arrs[i * cols + j];
            }
        }
        return arr;
    }

    public static Mat floatNDArrayToMat(NDArray ndArray,int C) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        Mat mat = new Mat(rows, cols, CvType.CV_32FC(C));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, ndArray.get(i, j).toFloatArray()); // 性能差，待优化
            }
        }

        return mat;

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

    public static Mat floatArrayToMat(float[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;
        Mat mat = new Mat(rows, cols, CvType.CV_32F);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arr[i][j]);
            }
        }

        return mat;
    }

    public static Mat uint8ArrayToMat(byte[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;
        Mat mat = new Mat(rows, cols, CvType.CV_8U);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.put(i, j, arr[i][j]);
            }
        }

        return mat;
    }
}