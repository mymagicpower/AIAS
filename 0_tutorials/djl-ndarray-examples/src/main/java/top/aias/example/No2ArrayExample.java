package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * NDarray Operations
 * NDarray 操作
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No2ArrayExample {

    private No2ArrayExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Number of dimensions of the array, initially with only one dimension.
            // 1. 数组的维数，最初只有一个维度。
            System.out.println("1. Number of dimensions of the array, initially with only one dimension.");
            NDArray nd = manager.arange(24);
            System.out.println(nd.getShape().dimension());
            // Resize it to have three dimensions.
            // 调整数组大小以使其具有三个维度。
            System.out.println("Resize it to have three dimensions.");
            nd = nd.reshape(2, 4, 3);
            System.out.println(nd.getShape().dimension());

            // 2. Shape of the array.
            // 2. 数组的形状。
            System.out.println("2. Shape of the array.");
            nd = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
            System.out.println(nd.getShape());

            // 3. Reshape the array.
            // 3. 重塑数组。
            System.out.println("3. Reshape the array.");
            nd = nd.reshape(3, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 4. Create an array filled with zeros.
            // 4. 创建一个全为零的数组。
            System.out.println("4. Create an array filled with zeros.");
            nd = manager.zeros(new Shape(5), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 5. Create an array filled with ones.
            // 5. 创建一个全为一的数组。
            System.out.println("5. Create an array filled with ones.");
            nd = manager.ones(new Shape(2, 2), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 6. Create an array from a range of values.
            // 6. 从一个值范围创建数组。
            // 6.1 Create an array from 0 to 5.
            // 6.1 创建一个从 0 到 5 的数组。
            System.out.println("6.1 Create an array from 0 to 5.");
            nd = manager.arange(5);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 6.2 Set the return type to float.
            // 6.2 将返回类型设置为浮点型。
            System.out.println("6.2 Set the return type to float.");
            nd = manager.arange(0, 5, 1, DataType.FLOAT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 6.3 Set the start, stop, and step values.
            // 6.3 设置起始值、终止值和步长。
            System.out.println("6.3 Set the start, stop, and step values.");
            nd = manager.arange(10, 20, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 6.4 Create an array of evenly spaced values using linspace.
            // 6.4 使用 linspace 创建一个等间距值的数组。
            System.out.println("6.4 Create an array of evenly spaced values using linspace.");
            nd = manager.linspace(1, 10, 10);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7. Array operations.
            // 7. 数组操作。
            // 7.1 Create an array from 0 to 5.
            // 7.1 创建一个从 0 到 5 的数组。
            System.out.println("7.1 Create an array from 0 to 5.");
            nd = manager.arange(12).reshape(3, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));
            nd = nd.transpose();
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7.2 Swap the two axes of the array.
            // 7.2 交换数组的两个轴。
            System.out.println("7.2 Swap the two axes of the array.");
            nd = manager.arange(8).reshape(2, 2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));
            nd = nd.swapAxes(2, 0);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7.3 Broadcasting - broadcast the array to a new shape.
            // 7.3 广播 - 将数组广播到新形状。
            System.out.println("7.3 Broadcasting - broadcast the array to a new shape.");
            nd = manager.arange(4).reshape(1, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));
            nd = nd.broadcast(new Shape(4, 4));
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7.4 Insert a new axis at a specified position to expand the shape of the array.
            // 7.4 在指定位置插入新轴以扩展数组形状。
            System.out.println("7.4 Insert a new axis at a specified position to expand the shape of the array.");
            NDArray x = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("Array x: ");
            System.out.println(x.toDebugString(100, 10, 100, 100, true));
            // Insert axis at position 0.
            // 在位置 0 插入轴。
            System.out.println("Insert axis at position 0.");
            NDArray y = x.expandDims(0);
            System.out.println("Array y:");
            System.out.println(y.toDebugString(100, 10, 100, 100, true));
            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());
            // Insert axis at position 1.
            // 在位置 1 插入轴。
            System.out.println("Insert axis at position 1.");
            y = x.expandDims(1);
            System.out.println("Array y after inserting axis at position 1:\n ");
            System.out.println(y.toDebugString(100, 10, 100, 100, true));

            System.out.println("x.ndim and y.ndim：");
            System.out.println(x.getShape().dimension() + " " + y.getShape().dimension());

            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.5 Remove a single-dimensional entry from the shape of a given array using squeeze.
            // 7.5 使用 squeeze 从给定数组的形状中移除单维条目。
            System.out.println("7.5 Remove a single-dimensional entry from the shape of a given array using squeeze.");
            x = manager.arange(9).reshape(1, 3, 3);
            System.out.println("Array x：");
            System.out.println(x.toDebugString(100, 10, 100, 100, true));

            y = x.squeeze();
            System.out.println("Array y: ");
            System.out.println(y.toDebugString(100, 10, 100, 100, true));

            System.out.println("Shapes of array x and y: ");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.6 Concatenate arrays using concatenate.
            // 7.6 使用 concatenate 连接数组。
            System.out.println("7.6 Concatenate arrays using concatenate.");
            NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100, true));

            NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("Second array: ");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            nd = NDArrays.concat(new NDList(a, b), 0);
            System.out.println("Concatenate the two arrays along axis 0: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            nd = NDArrays.concat(new NDList(a, b), 1);
            System.out.println("Concatenate the two arrays along axis 1: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7.7 Stack arrays in sequence along a new axis using stack.
            // 7.7 使用 stack 沿新轴按顺序堆叠数组。
            System.out.println("7.7 Stack arrays in sequence along a new axis using stack.");
            a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100, true));

            b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("Second array: ");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            nd = NDArrays.stack(new NDList(a, b), 0);
            System.out.println("Stack the two arrays along axis 0: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            nd = NDArrays.stack(new NDList(a, b), 1);
            System.out.println("Stack the two arrays along axis 1: ");
            System.out.println(nd.toDebugString(100, 10, 100, 100, true));

            // 7.8 Split an array into multiple sub-arrays along a specified axis using numpy.split.
            // 7.8 使用 numpy.split 沿指定轴将
            a = manager.arange(9);
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100, true));

            NDList list = a.split(3);
            System.out.println("Split the array into three equally sized sub-arrays: ");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100, true));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100, true));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100, true));

            list = a.split(new long[]{4, 7});
            // Split the array into multiple subarrays at the specified indices. For example, indices 4 and 7 will split the array into [0, 1, 2, 3], [4, 5, 6], and [7, 8, ...].
            // 按指定的索引将数组拆分为多个子数组。例如，索引 4 和 7 会将数组分割为 [0, 1, 2, 3]、[4, 5, 6] 和 [7, 8, ...]。
            System.out.println("Split the array at the positions indicated in a one-dimensional array: ");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100, true));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100, true));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100, true));

        }

    }
}
