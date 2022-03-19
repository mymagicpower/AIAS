package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

/**
 * Ndarray 统计函数
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No7StatisticExample {

    private No7StatisticExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 最小元素，最大元素
            NDArray a = manager.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 min() 函数：");
            NDArray b = a.min(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("再次调用 min() 函数：");
            b = a.min(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("调用 max() 函数：");
            b = a.max();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("再次调用 max() 函数：");
            b = a.max(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. 算术平均值
            a = manager.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println("我们的数组是：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("调用 mean() 函数：");
            b = a.mean();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 0 调用 mean() 函数：");
            b = a.mean(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("沿轴 1 调用 mean() 函数：");
            b = a.mean(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. 标准差 - std = sqrt(mean((x - x.mean())**2))
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean().sqrt();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. 方差 - mean((x - x.mean())** 2)
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean();
            System.out.println(b.toDebugString(100, 10, 100, 100));
        }
    }
}
