package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * Ndarray 矩阵
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No9MatrixExample {

    private No9MatrixExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 转置矩阵
            NDArray a = manager.arange(12).reshape(3, 4);
            System.out.println("原数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("转置数组：");
            NDArray b = a.transpose();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. 创建一个以 0 填充的矩阵 - zeros()
            a = manager.zeros(new Shape(2, 2));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 3. 创建一个以 1 填充的矩阵 - ones()
            a = manager.ones(new Shape(2, 2));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 4. 返回一个矩阵，对角线元素为 1，其他位置为零 - eye()
            a = manager.eye(3, 4, 0, DataType.INT32);
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 5. 创建一个给定大小的矩阵，数据是随机填充 - rand()
            a = manager.randomUniform(0, 1, new Shape(3, 3));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 6. 内积 - dot()
            a = manager.create(new int[][]{{1, 2}, {3, 4}});
            b = manager.create(new int[][]{{11, 12}, {13, 14}});
            NDArray c = a.dot(b);
            // 计算式为：
            // [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
            System.out.println(c.toDebugString(100, 10, 100, 100));

            // 7. 矩阵乘积 - matMul()
            a = manager.create(new int[][]{{1, 0}, {0, 1}});
            b = manager.create(new int[][]{{4, 1}, {2, 2}});
            c = a.matMul(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
        }
    }
}
