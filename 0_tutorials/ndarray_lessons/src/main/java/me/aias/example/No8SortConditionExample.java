package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * Ndarray 排序、条件刷选函数
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No8SortConditionExample {

    private No8SortConditionExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 数组的排序 - numpy.sort()
            NDArray a = manager.create(new int[][]{{3, 7}, {9, 1}});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 sort() 函数：");
            NDArray b = a.sort();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("按列排序：");
            b = a.sort(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. 数组值从小到大的索引值 - numpy.argsort()
            a = manager.create(new int[]{3, 1, 2});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 argsort() 函数：");
            b = a.argSort();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. 返回最大和最小元素的索引 - numpy.argmax() 和 numpy.argmin()
            a = manager.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 argmax() 函数：");
            b = a.argMax();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("展开数组：");
            b = a.flatten();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 0 的最大值索引：");
            b = a.argMax(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 1 的最大值索引：");
            b = a.argMax(1);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("调用 argmin() 函数：");
            b = a.argMin();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 0 的最大值索引：");
            b = a.argMin(0);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 1 的最大值索引：");
            b = a.argMin(1);
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. 数组中非零元素的索引 - numpy.nonzero()
            a = manager.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 nonzero() 函数：");
            b = a.nonzero();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 5. 数组中满足给定条件的元素的索引 - numpy.where()
            a = manager.arange(9f).reshape(3, 3);
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("大于 3 的元素的索引：");
            b = a.gt(3);
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("使用这些索引来获取满足条件的元素：");
            b = a.get(b);
            System.out.println(b.toDebugString(100, 10, 100, 100));
        }
    }
}
