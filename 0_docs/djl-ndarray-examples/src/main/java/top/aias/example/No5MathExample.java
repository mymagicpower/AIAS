package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;

/**
 * NDarray Mathematical Functions
 * NDarray数学函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No5MathExample {

    private No5MathExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Trigonometric functions
            // 1. 三角函数
            NDArray a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("Sine values for different angles: ");
            // 不同角度的正弦值：
            // Converted to radians by multiplying by pi/180
            // 通过乘以π/180转换为弧度
            NDArray b = a.mul(Math.PI / 180).sin();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Cosine values for angles in the array: ");
            // 数组中角度的余弦值：
            b = a.mul(Math.PI / 180).cos();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Tangent values for angles in the array: ");
            // 数组中角度的正切值：
            b = a.mul(Math.PI / 180).tan();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 2. Inverse trigonometric functions
            // 2. 反三角函数
            a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("Array containing sine values: ");
            // 包含正弦值的数组：
            NDArray sin = a.mul(Math.PI / 180).sin();
            System.out.println(sin.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calculating inverse sine of angles, returns values in radians: ");
            // 计算角度的反正弦值，返回以弧度为单位的值：
            NDArray inv = sin.asin();
            System.out.println(inv.toDebugString(100, 10, 100, 100, true));
            System.out.println("Checking the result by converting to degrees: ");
            // 通过转换为度数检查结果：
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("arccos and arctan functions behave similarly: ");
            // arccos和arctan函数的行为类似：
            NDArray cos = a.mul(Math.PI / 180).cos();
            System.out.println(cos.toDebugString(100, 10, 100, 100, true));
            System.out.println("Inverse cosine: ");
            // 反余弦：
            inv = cos.acos();
            System.out.println(inv.toDebugString(100, 10, 100, 100, true));
            System.out.println("Units in degrees: ");
            // 单位为度数：
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("tan function: ");
            // 正切函数：
            NDArray tan = a.mul(Math.PI / 180).tan();
            System.out.println(tan.toDebugString(100, 10, 100, 100, true));
            System.out.println("Inverse tangent: ");
            // 反正切：
            inv = tan.atan();
            System.out.println(inv.toDebugString(100, 10, 100, 100, true));
            System.out.println("Units in degrees: ");
            // 单位为度数：
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3. Rounding functions
            // 3. 舍入函数
            // 3.1 Rounding off
            // 3.1 四舍五入
            a = manager.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
            System.out.println("Original array: ");
            // 原始数组：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("After rounding off: ");
            // 四舍五入后的数组：
            b = a.round();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3.2 Floor function
            // 3.2 向下取整函数
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("Array provided: ");
            // 提供的数组：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Modified array: ");
            // 修改后的数组：
            b = a.floor();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3.3 Ceiling function
            // 3.3 向上取整函数
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("Array provided: ");
            // 提供的数组：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Modified array: ");
            // 修改后的数组：
            b = a.ceil();
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

        }

    }
}
