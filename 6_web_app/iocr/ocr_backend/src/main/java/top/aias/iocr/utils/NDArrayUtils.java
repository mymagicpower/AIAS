package top.aias.iocr.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;
/**
 * NDArray Utils 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class NDArrayUtils {
    /**
     * Sigmoid 激活函数
     *
     * @param input
     * @return
     */
    public static NDArray Sigmoid(NDArray input) {
        // Sigmoid 函数，即f(x)=1/(1+e-x)
        return input.neg().exp().add(1).pow(-1);
    }

    /**
     * np.arctan2和np.arctan都是计算反正切值的NumPy函数，但它们的参数和返回值不同。一般来说，np.arctan2的参数为(y, x)，
     * 返回值为[-π, π]之间的弧度值；而np.arctan的参数为x，返回值为[-π/2, π/2]之间的弧度值。两者之间的换算关系是：
     * np.arctan(y/x) = np.arctan2(y, x)（当x>0时），
     * 或  np.pi + np.arctan(y/x) = np.arctan2(y, x) （当x<0且y>=0时），
     * 或  np.pi - np.arctan(y/x) = np.arctan2(y, x) （当x<0且y<0时）。
     * @param y
     * @param x
     * @return
     */
    public static NDArray arctan2(NDArray y, NDArray x) {
        NDArray x_neg = x.lt(0).toType(DataType.INT32, false);
        NDArray y_pos = y.gte(0).toType(DataType.INT32, false);
        NDArray y_neg = y.lt(0).toType(DataType.INT32, false);

        NDArray theta = y.div(x).atan();
        // np.arctan(y/x) + np.pi = np.arctan2(y, x) （当x<0且y>=0时）
        theta = theta.add(x_neg.mul(y_pos).mul((float) Math.PI));
        // np.arctan(y/x) - np.pi = np.arctan2(y, x) （当x<0且y<0时）
        theta = theta.add(x_neg.mul(y_neg).mul(-(float) Math.PI));

        theta = theta.mul(180).div((float) Math.PI);

        return theta;
    }

    /**
     * 最大池化
     *
     * @param manager
     * @param heat
     * @param ksize
     * @param stride
     * @param padding
     * @return
     */
    public static NDArray maxPool(NDManager manager, NDArray heat, int ksize, int stride, int padding) {
        int rows = (int) (heat.getShape().get(0));
        int cols = (int) (heat.getShape().get(1));
        // hmax = F.max_pool2d( heat, (ksize, ksize), stride=1, padding=(ksize-1)//2)
        NDArray max_pool2d = manager.zeros(new Shape(rows + 2 * padding, cols + 2 * padding));
        max_pool2d.set(new NDIndex(padding + ":" + (rows + padding) + ","+ padding + ":" + (cols + padding)), heat);
        float[][] max_pool2d_arr = NDArrayUtils.floatNDArrayToArray(max_pool2d);
        float[][] arr = new float[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float max = max_pool2d_arr[row][col];
                for (int i = row; i < row + ksize; i++) {
                    for (int j = col; j < col + ksize; j++) {
                        if (max_pool2d_arr[i][j] > max) {
                            max = max_pool2d_arr[i][j];
                        }
                    }
                }
                arr[row][col] = max;
            }
        }

        NDArray hmax = manager.create(arr).reshape(rows, cols);
        return  hmax;
    }

    /**
     * mat To MatOfPoint
     *
     * @param mat
     * @return
     */
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

    /**
     * int NDArray To int[][] Array
     *
     * @param ndArray
     * @return
     */
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

    /**
     * float NDArray To float[][] Array
     *
     * @param ndArray
     * @return
     */
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

    /**
     * mat To double[][] Array
     *
     * @param mat
     * @return
     */
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

    /**
     *  mat To float[][] Array
     *
     * @param mat
     * @return
     */
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

    /**
     * mat To byte[][] Array
     *
     * @param mat
     * @return
     */
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

    /**
     * float NDArray To Mat
     *
     * @param ndArray
     * @param cvType
     * @return
     */
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

    /**
     * float NDArray To Mat
     *
     * @param ndArray
     * @return
     */
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

    /**
     * uint8 NDArray To Mat
     *
     * @param ndArray
     * @return
     */
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

    /**
     * float[][] Array To Mat
     * @param arr
     * @return
     */
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

    /**
     * uint8Array To Mat
     * @param arr
     * @return
     */
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


    /**
     * list 转 Mat
     *
     * @param points
     * @return
     */
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
