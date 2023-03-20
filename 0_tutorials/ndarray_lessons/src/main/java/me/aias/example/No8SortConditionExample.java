package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * NDarray Sorting and Conditional Filtering Functions
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
            // 1.Array Sorting - numpy.sort()
            NDArray a = manager.create(new int[][]{{3, 7}, {9, 1}});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling sort() function: ");
            NDArray b = a.sort();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Sort along columns: ");
            b = a.sort(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. Indexes of values in array sorted from smallest to largest: - numpy.argsort()
            a = manager.create(new int[]{3, 1, 2});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling argsort() function: ");
            b = a.argSort();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. Indexes of maximum and minimum elements - numpy.argmax() 和 numpy.argmin()
            a = manager.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling argmax() function: ");
            b = a.argMax();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Flattening the array: ");
            b = a.flatten();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Index of the maximum value along axis 0: ");
            b = a.argMax(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Index of the maximum value along axis 1: ");
            b = a.argMax(1);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling argmin() function: ");
            b = a.argMin();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Index of the minimum value along axis 0: ");
            b = a.argMin(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Index of the minimum value along axis 1: ");
            b = a.argMin(1);
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. Indexes of non-zero elements in the array - numpy.nonzero()
            a = manager.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling nonzero() function：");
            b = a.nonzero();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 5. Indexes of elements in the array satisfying a given condition - numpy.where()
            a = manager.arange(9f).reshape(3, 3);
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Indexes of elements greater than 3: ");
            b = a.gt(3);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Use these indexes to get the elements satisfying the condition: ");
            b = a.get(b);
            System.out.println(b.toDebugString(100, 10, 100, 100));
        }
    }
}
