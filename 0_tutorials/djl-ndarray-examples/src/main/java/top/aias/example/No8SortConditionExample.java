package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * NDarray Sorting and Conditional Filtering Functions
 * NDarray 排序和条件筛选功能
 * http://aias.top/
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No8SortConditionExample {

    private No8SortConditionExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Array Sorting - numpy.sort()
            // 1. 数组排序 - numpy.sort()
            NDArray a = manager.create(new int[][]{{3, 7}, {9, 1}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling sort() function: ");
            // 调用 sort() 函数：
            NDArray b = a.sort();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Sort along columns: ");
            // 按列排序：
            b = a.sort(0);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 2. Indexes of values in array sorted from smallest to largest: - numpy.argsort()
            // 2. 按从小到大排序的数组值的索引：- numpy.argsort()
            a = manager.create(new int[]{3, 1, 2});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling argsort() function: ");
            // 调用 argsort() 函数：
            b = a.argSort();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3. Indexes of maximum and minimum elements - numpy.argmax() 和 numpy.argmin()
            // 3. 最大和最小元素的索引 - numpy.argmax() 和 numpy.argmin()
            a = manager.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling argmax() function: ");
            // 调用 argmax() 函数：
            b = a.argMax();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Flattening the array: ");
            // 展平数组：
            b = a.flatten();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Index of the maximum value along axis 0: ");
            // 沿轴 0 的最大值的索引：
            b = a.argMax(0);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Index of the maximum value along axis 1: ");
            // 沿轴 1 的最大值的索引：
            b = a.argMax(1);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling argmin() function: ");
            // 调用 argmin() 函数：
            b = a.argMin();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Index of the minimum value along axis 0: ");
            // 沿轴 0 的最小值的索引：
            b = a.argMin(0);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Index of the minimum value along axis 1: ");
            // 沿轴 1 的最小值的索引：
            b = a.argMin(1);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 4. Indexes of non-zero elements in the array - numpy.nonzero()
            // 4. 数组中非零元素的索引 - numpy.nonzero()
            a = manager.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling nonzero() function：");
            // 调用 nonzero() 函数：
            b = a.nonzero();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 5. Indexes of elements in the array satisfying a given condition - numpy.where()
            // 5. 满足给定条件的数组元素的索引 - numpy.where()
            a = manager.arange(9f).reshape(3, 3);
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Indexes of elements greater than 3: ");
            // 大于 3 的元素的索引：
            b = a.gt(3);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Use these indexes to get the elements satisfying the condition: ");
            // 使用这些索引获取满足条件的元素：
            b = a.get(b);
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
        }
    }
}
