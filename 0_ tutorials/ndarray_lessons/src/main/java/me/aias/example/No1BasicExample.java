package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * Ndarray 基本操作
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No1BasicExample {

    private No1BasicExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 创建数据对象 - 向量
            int[] vector = new int[]{1, 2, 3};
            NDArray nd = manager.create(vector);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.1 创建数据对象 - 矩阵
            int[][] mat = new int[][]{{1, 2}, {3, 4}};
            nd = manager.create(mat);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.2 创建数据对象 - 矩阵
            int[] arr = new int[]{1, 2, 3, 4};
            nd = manager.create(arr).reshape(2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.3 创建数据对象 - 指定矩阵维度
            nd = manager.create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
            System.out.println(nd.toDebugString(100, 10, 100, 100));


            // 3. 数据类型转换
            nd = manager.create(new int[]{1, 2, 3, 4}).toType(DataType.FLOAT32, false);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

        }

    }
}
