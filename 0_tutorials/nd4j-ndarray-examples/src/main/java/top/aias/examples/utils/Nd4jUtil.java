package top.aias.examples.utils;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

/**
 * INDArray Utils
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class Nd4jUtil {
    /**
     * 在指定轴插入一个新维度
     *
     * Inserts a new dimension at the specified axis.
     *
     * @param input 输入 INDArray
     * @param axis  要插入维度的轴
     * @return 插入新维度后的 INDArray
     *         Returns the INDArray with the new dimension inserted.
     */
    public static INDArray expandDims(INDArray input, int axis) {
        // 获取当前形状
        // Get the current shape
        long[] originalShape = input.shape();
        int rank = originalShape.length;

        // 验证轴是否有效
        // Validate if the axis is valid
        if (axis < 0 || axis > rank) {
            throw new IllegalArgumentException("Invalid axis: " + axis + ". Must be in range [0, " + rank + "].");
        }

        // 构建新形状，插入 1 作为新维度
        // Construct the new shape, inserting 1 as the new dimension
        long[] newShape = new long[rank + 1];
        for (int i = 0, j = 0; i < newShape.length; i++) {
            if (i == axis) {
                newShape[i] = 1; // 插入新维度
                // Insert the new dimension
            } else {
                newShape[i] = originalShape[j++];
            }
        }

        // 调整形状
        // Reshape the array
        return input.reshape(newShape);
    }

    /**
     * 移除 INDArray 中所有长度为 1 的维度
     *
     * Removes all dimensions with length 1 from the INDArray.
     *
     * @param input 输入 INDArray
     * @return 移除长度为 1 的维度后的 INDArray
     *         Returns the INDArray after removing dimensions of length 1.
     */
    public static INDArray squeeze(INDArray input) {
        // 获取非 1 的维度
        // Get the dimensions that are not equal to 1
        long[] originalShape = input.shape();
        long[] newShape = java.util.Arrays.stream(originalShape)
                .filter(dim -> dim != 1)
                .toArray();

        // 如果所有维度都是 1，则返回标量
        // If all dimensions are 1, return a scalar
        if (newShape.length == 0) {
            return Nd4j.scalar(input.getDouble(0));
        }

        // 调整为新形状
        // Reshape to the new shape
        return input.reshape(newShape);
    }

    /**
     * 将 INDArray 按第 0 维分割为指定数量的子数组
     *
     * Splits the INDArray along the 0th dimension into the specified number of subarrays.
     *
     * @param input     输入 INDArray
     * @param numSplits 分割的数量
     * @return 分割后的子数组
     *         Returns the split subarrays.
     */
    public static INDArray[] split(INDArray input, int numSplits) {
        // 检查是否能被均匀分割
        // Check if it can be split evenly
        long length = input.length();
        if (length % numSplits != 0) {
            throw new IllegalArgumentException("Array cannot be evenly split into " + numSplits + " parts.");
        }

        // 计算每个子数组的长度
        // Calculate the length of each subarray
        long splitSize = length / numSplits;
        INDArray[] splits = new INDArray[numSplits];

        // 分割数组
        // Split the array
        for (int i = 0; i < numSplits; i++) {
            splits[i] = input.get(NDArrayIndex.interval(i * splitSize, (i + 1) * splitSize));
        }

        return splits;
    }

    /**
     * 按指定索引分割 INDArray
     *
     * Splits the INDArray at the specified indices.
     *
     * @param input 输入 INDArray
     * @param indices 分割点的索引
     * @return 分割后的子数组
     *         Returns the split subarrays.
     */
    public static INDArray[] splitWithIndices(INDArray input, long[] indices) {
        int numSplits = indices.length + 1;
        INDArray[] splits = new INDArray[numSplits];

        // 起始点
        // Starting point
        long start = 0;

        // 遍历分割点
        // Iterate over the split points
        for (int i = 0; i < indices.length; i++) {
            splits[i] = input.get(NDArrayIndex.interval(start, indices[i]));
            start = indices[i];
        }

        // 最后一段
        // The last segment
        splits[indices.length] = input.get(NDArrayIndex.interval(start, input.length()));

        return splits;
    }
}
