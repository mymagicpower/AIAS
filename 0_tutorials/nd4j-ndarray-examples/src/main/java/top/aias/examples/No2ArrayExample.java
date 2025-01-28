package top.aias.examples;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import top.aias.examples.utils.Nd4jUtil;

import java.util.Arrays;

/**
 * INDArray Operations
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No2ArrayExample {

    private No2ArrayExample() {
    }

    public static void main(String[] args) {

        // 1. Number of dimensions of the array, initially with only one dimension.
        // 1. 数组的维度数，最初只有一个维度。
        System.out.println("1. Number of dimensions of the array, initially with only one dimension.");
        INDArray nd = Nd4j.arange(24);
        System.out.println(nd.shape().length);
        // Resize it to have three dimensions.
        // 将其调整为三维。
        System.out.println("Resize it to have three dimensions.");
        nd = nd.reshape(2, 4, 3);
        System.out.println(nd.shape().length);

        // 2. Shape of the array.
        // 2. 数组的形状。
        System.out.println("2. Shape of the array.");
        nd = Nd4j.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
        System.out.println(Arrays.toString(nd.shape()));

        // 3. Reshape the array.
        // 3. 重塑数组。
        System.out.println("3. Reshape the array.");
        nd = nd.reshape(3, 2);
        System.out.println(nd);

        // 4. Create an array filled with zeros.
        // 4. 创建一个填充零的数组。
        System.out.println("4. Create an array filled with zeros.");
        nd = Nd4j.zeros(DataType.INT32, 5);
        System.out.println(nd);

        // 5. Create an array filled with ones.
        // 5. 创建一个填充一的数组。
        System.out.println("5. Create an array filled with ones.");
        nd = Nd4j.ones(DataType.INT32, 2, 2);
        System.out.println(nd);

        // 6. Create an array from a range of values.
        // 6. 从一系列值中创建数组。
        // 6.1 Create an array from 0 to 5.
        // 6.1 创建一个从 0 到 5 的数组。
        System.out.println("6.1 Create an array from 0 to 5.");
        nd = Nd4j.arange(5);
        System.out.println(nd);

        // 6.2 Set the return type to float.
        // 6.2 设置返回类型为 float。
        System.out.println("6.2 Set the return type to float.");
        nd = Nd4j.arange(0, 5, 1);
        INDArray ndFloat = nd.castTo(DataType.FLOAT);
        System.out.println(ndFloat);

        // 6.3 Set the start, stop, and step values.
        // 6.3 设置起始、停止和步长值。
        System.out.println("6.3 Set the start, stop, and step values.");
        nd = Nd4j.arange(10, 20, 2);
        System.out.println(nd);

        // 6.4 Create an array of evenly spaced values using linspace.
        // 6.4 使用 linspace 创建一个均匀分布的数组。
        System.out.println("6.4 Create an array of evenly spaced values using linspace.");
        nd = Nd4j.linspace(1, 10, 10);
        System.out.println(nd);

        // 7. Array operations.
        // 7. 数组操作。
        // 7.1 Create an array from 0 to 5.
        // 7.1 创建一个从 0 到 5 的数组。
        System.out.println("7.1 Create an array from 0 to 5.");
        nd = Nd4j.arange(12).reshape(3, 4);
        System.out.println(nd);
        nd = nd.transpose();
        System.out.println(nd);

        // 7.2 Swap the two axes of the array.
        // 7.2 交换数组的两个轴。
        System.out.println("7.2 Swap the two axes of the array.");
        nd = Nd4j.arange(8).reshape(2, 2, 2);
        System.out.println(nd);
        nd = nd.swapAxes(2, 0);
        System.out.println(nd);

        // 7.3 Broadcasting - broadcast the array to a new shape.
        // 7.3 广播 - 将数组广播到新形状。
        System.out.println("7.3 Broadcasting - broadcast the array to a new shape.");
        nd = Nd4j.arange(4).reshape(1, 4);
        System.out.println(nd);
        nd = nd.broadcast(4, 4);
        System.out.println(nd);

        // 7.4 Insert a new axis at a specified position to expand the shape of the array.
        // 7.4 在指定位置插入一个新轴以扩展数组的形状。
        System.out.println("7.4 Insert a new axis at a specified position to expand the shape of the array.");
        INDArray x = Nd4j.create(new int[][]{{1, 2}, {3, 4}});
        System.out.println("Array x: ");
        System.out.println(x);

        // Insert axis at position 0.
        // 使用 reshape 方法在第 0 维添加一个维度
        System.out.println("Insert axis at position 0.");
        INDArray y = Nd4jUtil.expandDims(x, 0);
        System.out.println("Array y:");
        System.out.println(y);
        System.out.println("Shapes of array x and y: ");
        System.out.println(Arrays.toString(x.shape()) + " " + Arrays.toString(y.shape()));
        // Insert axis at position 1.
        // 在位置 1 插入轴。
        System.out.println("Insert axis at position 1.");
        y = Nd4jUtil.expandDims(x, 1);
        System.out.println("Array y after inserting axis at position 1:\n ");
        System.out.println(y);

        System.out.println("x.ndim and y.ndim：");
        System.out.println(x.shape().length + " " + y.shape().length);

        System.out.println("Shapes of array x and y: ");
        System.out.println(Arrays.toString(x.shape()) + " " + Arrays.toString(y.shape()));

        // 7.5 Remove a single-dimensional entry from the shape of a given array using squeeze.
        // 7.5 使用 squeeze 从给定数组的形状中移除单一维度。
        System.out.println("7.5 Remove a single-dimensional entry from the shape of a given array using squeeze.");
        x = Nd4j.arange(9).reshape(1, 3, 3);
        System.out.println("Array x：");
        System.out.println(x);

        y = Nd4jUtil.squeeze(x);
        System.out.println("Array y: ");
        System.out.println(y);

        System.out.println("Shapes of array x and y: ");
        System.out.println(Arrays.toString(x.shape()) + " " + Arrays.toString(y.shape()));

        // 7.6 Concatenate arrays using concatenate.
        // 7.6 使用 concatenate 连接数组。
        System.out.println("7.6 Concatenate arrays using concatenate.");
        INDArray a = Nd4j.create(new int[][]{{1, 2}, {3, 4}});
        System.out.println("First array: ");
        System.out.println(a);

        INDArray b = Nd4j.create(new int[][]{{5, 6}, {7, 8}});
        System.out.println("Second array: ");
        System.out.println(b);


        nd = Nd4j.concat(0, a, b); // Nd4j.vstack(a,b)
        System.out.println("Concatenate the two arrays along axis 0: ");
        System.out.println(nd);

        nd = Nd4j.concat(1, a, b); // Nd4j.hstack(a,bs);
        System.out.println("Concatenate the two arrays along axis 1: ");
        System.out.println(nd);

        // 7.7 Stack arrays in sequence along a new axis using stack.
        // 7.7 使用 stack 按顺序在新轴上堆叠数组。
        System.out.println("7.7 Stack arrays in sequence along a new axis using stack.");
        a = Nd4j.create(new int[][]{{1, 2}, {3, 4}});
        System.out.println("First array: ");
        System.out.println(a);

        b = Nd4j.create(new int[][]{{5, 6}, {7, 8}});
        System.out.println("Second array: ");
        System.out.println(b);

        nd = Nd4j.stack(0, a, b);
        System.out.println("Stack the two arrays along axis 0: ");
        System.out.println(nd);

        nd = Nd4j.stack(1, a, b);
        System.out.println("Stack the two arrays along axis 1: ");
        System.out.println(nd);

        // 7.8 Split an array into multiple sub-arrays along a specified axis using numpy.split.
        // 7.8 使用 numpy.split 将数组按指定轴拆分成多个子数组。System.out.println("7.8 Split an array into multiple sub-arrays along a specified axis using numpy.split.");
        System.out.println("7.8 Split an array into multiple sub-arrays along a specified axis using numpy.split.");
        a = Nd4j.arange(9);
        System.out.println("First array: ");
        System.out.println(a);

        // Split the array into 3 subarrays by length
        // 按长度分割数组为 3 个子数组
        INDArray[] splits = Nd4jUtil.split(a, 3);
        System.out.println("Split the array into three equally sized sub-arrays: ");
        System.out.println(splits[0]);
        System.out.println(splits[1]);
        System.out.println(splits[2]);

        // Split the array into multiple subarrays at the specified indices. For example, indices 4 and 7 will split the array into [0, 1, 2, 3], [4, 5, 6], and [7, 8, ...].
        // 按指定的索引将数组拆分为多个子数组。例如，索引 4 和 7 会将数组分割为 [0, 1, 2, 3]、[4, 5, 6] 和 [7, 8, ...]。
        splits = Nd4jUtil.splitWithIndices(a, new long[]{4, 7});
        System.out.println(splits[0]);
        System.out.println(splits[1]);
        System.out.println(splits[2]);

    }
}


