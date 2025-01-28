package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

/**
 * NDarray Statistical Functions
 * NDarray 统计函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No7StatisticExample {

    private No7StatisticExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1.Minimum and Maximum Elements
            // 1. 最小值和最大值元素
            NDArray a = manager.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling min() function: ");
            // 调用 min() 函数：
            NDArray b = a.min(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling min() function again: ");
            // 再次调用 min() 函数：
            b = a.min(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling max() function: ");
            // 调用 max() 函数：
            b = a.max();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling max() function again: ");
            // 再次调用 max() 函数：
            b = a.max(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 2. Arithmetic Mean
            // 2. 算术平均值
            a = manager.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling mean() function: ");
            // 调用 mean() 函数：
            b = a.mean();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling mean() function along axis 0: ");
            // 按轴 0 调用 mean() 函数：
            b = a.mean(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling mean() function along axis 1: ");
            // 按轴 1 调用 mean() 函数：
            b = a.mean(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3. Standard Deviation - std = sqrt(mean((x - x.mean())**2))
            // 3. 标准差 - std = sqrt(mean((x - x.mean())**2))
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean().sqrt();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 4. Variance - mean((x - x.mean())** 2)
            // 4. 方差 - mean((x - x.mean())** 2)
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
        }
    }
}
