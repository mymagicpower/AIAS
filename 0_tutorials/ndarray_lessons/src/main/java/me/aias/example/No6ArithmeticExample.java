package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;

/**
 * NDarray Arithmetic Functions
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
            NDArray a = manager.arange(0, 9, 1, DataType.FLOAT32).reshape(3, 3);
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Second array: ");
            NDArray b = manager.create(new int[]{10, 10, 10});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Addition of two arrays: ");
            NDArray c = a.add(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
            System.out.println("Subtraction of two arrays: ");
            c = a.sub(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
            System.out.println("Multiplication of two arrays: ");
            c = a.mul(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
            System.out.println("Division of two arrays: ");
            c = a.div(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));

            // 2. Power function
            a = manager.create(new int[]{10, 20, 30});
            System.out.println("First array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Second array: ");
            b = manager.create(new int[]{3, 5, 7});
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Calling the mod() function: ");
            c = a.mod(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
        }

    }
}
