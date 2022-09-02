package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;

/**
 * Ndarray 数学函数
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No5MathExample {

    private No5MathExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 三角函数
            NDArray a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("不同角度的正弦值：");
            // 通过乘 pi/180 转化为弧度
            NDArray b = a.mul(Math.PI / 180).sin();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("数组中角度的余弦值：");
            b = a.mul(Math.PI / 180).cos();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("数组中角度的正切值：");
            b = a.mul(Math.PI / 180).tan();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. 反三角函数
            a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("含有正弦值的数组：");
            NDArray sin = a.mul(Math.PI / 180).sin();
            System.out.println(sin.toDebugString(100, 10, 100, 100));
            System.out.println("计算角度的反正弦，返回值以弧度为单位：");
            NDArray inv = sin.asin();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("通过转化为角度制来检查结果：");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("arccos 和 arctan 函数行为类似：");
            NDArray cos = a.mul(Math.PI / 180).cos();
            System.out.println(cos.toDebugString(100, 10, 100, 100));
            System.out.println("反余弦：");
            inv = cos.acos();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("角度制单位：");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("tan 函数：");
            NDArray tan = a.mul(Math.PI / 180).tan();
            System.out.println(tan.toDebugString(100, 10, 100, 100));
            System.out.println("反正切：");
            inv = tan.atan();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("角度制单位：");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. 舍入函数
            // 3.1 四舍五入
            a = manager.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
            System.out.println("原数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("舍入后：");
            b = a.round();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.2 向下取整
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("提供的数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("修改后的数组：");
            b = a.floor();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.3 向上取整
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("提供的数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("修改后的数组：");
            b = a.ceil();
            System.out.println(b.toDebugString(100, 10, 100, 100));

        }

    }
}
