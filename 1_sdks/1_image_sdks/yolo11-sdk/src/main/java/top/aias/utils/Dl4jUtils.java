package top.aias.utils;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.FloatBuffer;

public class Dl4jUtils {
    public static INDArray matToBgrINDArray(Mat mat) {
        // 确保 Mat 是连续的内存块
        if (!mat.isContinuous()) {
            throw new IllegalArgumentException("Mat must be continuous.");
        }

        // 获取 Mat 的数据
        int rows = mat.rows();
        int cols = mat.cols();
        int channels = mat.channels();
        byte[] data = new byte[(int) (mat.total() * mat.elemSize())];
        mat.get(0, 0, data);

        // 将 byte[] 转换为 float[]，并归一化到 [0, 1]
        float[] floatData = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            floatData[i] = Byte.toUnsignedInt(data[i]);
        }

        // 创建 INDArray，形状为 [rows, cols, channels]（HWC 格式）
        INDArray hwcArray = Nd4j.create(floatData, new int[]{rows, cols, channels}, 'c');

        // 转换为 CHW 格式
        INDArray chwArray = hwcArray.permute(2, 0, 1);

        return chwArray;
    }

    public static INDArray matToRgbINDArray(Mat mat) {
        // 确保 Mat 是连续的内存块
        if (!mat.isContinuous()) {
            throw new IllegalArgumentException("Mat must be continuous.");
        }

        // 创建一个空的 Mat 来存储结果
        Mat rgbMat = new Mat();
        // 使用 OpenCV 的 cvtColor 函数将 BGR 转换为 RGB
        Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_BGR2RGB);

        // 获取 Mat 的数据
        int rows = rgbMat.rows();
        int cols = rgbMat.cols();
        int channels = mat.channels();
        byte[] data = new byte[(int) (rgbMat.total() * rgbMat.elemSize())];
        rgbMat.get(0, 0, data);

        // 将 byte[] 转换为 float[]，并归一化到 [0, 1]
        float[] floatData = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            floatData[i] = Byte.toUnsignedInt(data[i]); // / 255.0f
        }

        // 创建 INDArray，形状为 [rows, cols, channels]（HWC 格式）
        INDArray hwcArray = Nd4j.create(floatData, new int[]{rows, cols, channels}, 'c');

        // 转换为 CHW 格式
        INDArray chwArray = hwcArray.permute(2, 0, 1);

        return chwArray;
    }

    // 方法有问题，读取数据的顺序有问题
    public static OnnxTensor convertToOnnxTensor(OrtEnvironment env, INDArray indArray) throws OrtException {
        // 确保 INDArray 是以 float 为数据类型
        if (indArray.dataType() != org.nd4j.linalg.api.buffer.DataType.FLOAT) {
            throw new IllegalArgumentException("INDArray must be of type FLOAT.");
        }

        // 获取 INDArray 的形状
        long[] shape = indArray.shape();

        // 提取 INDArray 的数据为 FloatBuffer（扁平化数据）
        FloatBuffer floatBuffer = FloatBuffer.wrap(indArray.data().asFloat());

        // 创建 OnnxTensor
        return OnnxTensor.createTensor(env, floatBuffer, shape);
    }
    // 方法有问题，读取数据的顺序有问题
    public static OnnxTensor convertToOnnxTensor2(OrtEnvironment env, INDArray indArray) throws OrtException {
        // Ensure INDArray is in the correct format (float type)
        INDArray array = indArray.castTo(Nd4j.dataType().FLOAT);
        // Get the shape of the INDArray
        long[] shape = array.shape();
        // Convert INDArray data to a FloatBuffer
        FloatBuffer buffer = FloatBuffer.allocate((int) array.length());
        float[] data = array.data().asFloat(); // Extract data as float array
        for (float value : data) {
            buffer.put(value);
        }
        buffer.flip(); // Prepare buffer for reading

        // Create an OnnxTensor from the FloatBuffer
        return OnnxTensor.createTensor(env, buffer, shape);
    }

    /**
     * 将 BGR 图像转换为 RGB 图像
     *
     * @param bgrImage BGR 顺序的 INDArray 图像
     * @return RGB 顺序的 INDArray 图像
     */
    public static INDArray convertBGRToRGB(INDArray bgrImage) {
        // 获取图像的形状
        long[] shape = bgrImage.shape();

        // 创建一个新的 INDArray 用于存储 RGB 图像
        INDArray rgbImage = Nd4j.create(shape);

        // 将 BGR 通道的顺序转换为 RGB 顺序
        for (int i = 0; i < shape[1]; i++) {
            for (int j = 0; j < shape[2]; j++) {
                // 获取 BGR 值
                float b = bgrImage.getFloat(0, i, j);  // B 通道
                float g = bgrImage.getFloat(1, i, j);  // G 通道
                float r = bgrImage.getFloat(2, i, j);  // R 通道

                // 设置 RGB 值
                rgbImage.put(new INDArrayIndex[]{NDArrayIndex.point(0), NDArrayIndex.point(i), NDArrayIndex.point(j)}, r);
                rgbImage.put(new INDArrayIndex[]{NDArrayIndex.point(1), NDArrayIndex.point(i), NDArrayIndex.point(j)}, g);
                rgbImage.put(new INDArrayIndex[]{NDArrayIndex.point(2), NDArrayIndex.point(i), NDArrayIndex.point(j)}, b);
            }
        }

        return rgbImage;
    }

    /**
     * 将 INDArray 转换为 4 维数组
     *
     * @param indArray 输入的 INDArray
     * @return 转换后的 4 维数组
     */
    public static float[][][][] convertINDArrayTo4DArray(INDArray indArray) {
        // 获取 INDArray 的形状
        long[] shape = indArray.shape();

        // 确保是 4 维的
        if (shape.length != 4) {
            throw new IllegalArgumentException("Input INDArray must be 4-dimensional");
        }

        // 创建一个 4 维数组
        float[][][][] result = new float[(int) shape[0]][(int) shape[1]][(int) shape[2]][(int) shape[3]];

        // 将 INDArray 的数据复制到 4 维数组中
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                for (int k = 0; k < shape[2]; k++) {
                    for (int l = 0; l < shape[3]; l++) {
                        result[i][j][k][l] = indArray.getFloat(i, j, k, l);
                    }
                }
            }
        }

        return result;
    }
}
