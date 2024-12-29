package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

/**
 * NDarray Arithmetic Functions
 * NDarray 算术函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No6ArithmeticExample {

    private No6ArithmeticExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Addition, subtraction, multiplication, and division
            // 1. 加法、减法、乘法和除法
            NDArray a = manager.arange(0, 9, 1, DataType.FLOAT32).reshape(3, 3);
            System.out.println("First array: ");
            // 第一个数组：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Second array: ");
            // 第二个数组：
            NDArray b = manager.create(new int[]{10, 10, 10});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Addition of two arrays: ");
            // 两个数组的加法：
            NDArray c = a.add(b);
            System.out.println(c.toDebugString(100, 10, 100, 100, true));
            System.out.println("Subtraction of two arrays: ");
            // 两个数组的减法：
            c = a.sub(b);
            System.out.println(c.toDebugString(100, 10, 100, 100, true));
            System.out.println("Multiplication of two arrays: ");
            // 两个数组的乘法：
            c = a.mul(b);
            System.out.println(c.toDebugString(100, 10, 100, 100, true));
            System.out.println("Division of two arrays: ");
            // 两个数组的除法：
            c = a.div(b);
            System.out.println(c.toDebugString(100, 10, 100, 100, true));

            // 2. Power function
            // 2. 幂函数
            a = manager.create(new int[]{10, 20, 30});
            System.out.println("First array: ");
            // 第一个数组：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Second array: ");
            // 第二个数组：
            b = manager.create(new int[]{3, 5, 7});
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Calling the mod() function: ");
            // 调用 mod() 函数：
            c = a.mod(b);
            System.out.println(c.toDebugString(100, 10, 100, 100, true));
        }

    }
}
