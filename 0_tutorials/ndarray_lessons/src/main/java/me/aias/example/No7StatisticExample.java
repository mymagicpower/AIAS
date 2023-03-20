package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

/**
 * NDarray Statistical Functions
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
            NDArray a = manager.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling min() function: ");
            NDArray b = a.min(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling min() function again: ");
            b = a.min(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling max() function: ");
            b = a.max();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling max() function again: ");
            b = a.max(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. Arithmetic Mean
            a = manager.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println("Our array is: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Calling mean() function: ");
            b = a.mean();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling mean() function along axis 0: ");
            b = a.mean(new int[]{0});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling mean() function along axis 1: ");
            b = a.mean(new int[]{1});
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. Standard Deviation - std = sqrt(mean((x - x.mean())**2))
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean().sqrt();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. Variance - mean((x - x.mean())** 2)
            a = manager.create(new float[]{1,2,3,4});
            b = a.sub(a.mean()).pow(2).mean();
            System.out.println(b.toDebugString(100, 10, 100, 100));
        }
    }
}
