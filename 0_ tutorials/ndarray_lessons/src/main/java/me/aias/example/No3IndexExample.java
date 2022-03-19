package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;

/**
 * Ndarray 索引切片
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No3IndexExample {

    private No3IndexExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 通过索引或切片来访问和修改ndarray对象的内容
            NDArray a = manager.arange(10);
            NDArray b = a.get("2:7:2");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. 冒号 :的使用
            // 实例 2.1
            a = manager.arange(10);
            b = a.get("5");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // 实例 2.2
            a = manager.arange(10);
            b = a.get("2:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // 实例 2.3
            a = manager.arange(10);
            b = a.get("2:5");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.1 多维数组索引
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("从数组索引 a[1:] 处开始切割：");
            b = a.get("1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.2 多维数组索引,省略号 …(或者冒号:)
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("第2列元素：");
            b = a.get("...,1");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("第2行元素：");
            b = a.get("1,...");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("第2列及剩下的所有元素：");
            b = a.get("...,1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // 冒号：
            System.out.println("第2列元素：");
            b = a.get(":,1");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("第2行元素：");
            b = a.get("1,:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("第2列及剩下的所有元素：");
            b = a.get(":,1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. 布尔索引
            NDArray x = manager.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
            System.out.println("我们的数组是：");
            System.out.println(x.toDebugString(100, 10, 100, 100));
            System.out.println("大于 5 的元素是：");
            NDArray y = x.get(x.gt(5));
            System.out.println(y.toDebugString(100, 10, 100, 100));



        }

    }
}
