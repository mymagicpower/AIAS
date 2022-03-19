package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

/**
 * Ndarray 索引切片
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No4BroadcastExample {

    private No4BroadcastExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1.两个数组 a 和 b 形状相同
            NDArray x = manager.create(new int[]{1, 2, 3, 4});
            NDArray y = manager.create(new int[]{10, 20, 30, 40});
            NDArray z = x.mul(y);
            System.out.println(z.toDebugString(100, 10, 100, 100));

            // 2. 当运算中的 2 个数组的形状不同时，将自动触发广播机制
            x = manager.create(new int[][]{{0, 0, 0}, {10, 10, 10}, {20, 20, 20}, {30, 30, 30}});
            y = manager.create(new int[]{1, 2, 3});
            z = x.add(y);
            System.out.println(z.toDebugString(100, 10, 100, 100));
        }

    }
}
