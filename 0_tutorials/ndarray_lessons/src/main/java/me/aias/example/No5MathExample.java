package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;

/**
 * NDarray Mathematical Functions
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
            NDArray a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("Sine values for different angles: ");
            // Converted to radians by multiplying by pi/180
            NDArray b = a.mul(Math.PI / 180).sin();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Cosine values for angles in the array: ");
            b = a.mul(Math.PI / 180).cos();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Tangent values for angles in the array: ");
            b = a.mul(Math.PI / 180).tan();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. Inverse trigonometric functions
            a = manager.create(new int[]{0, 30, 45, 60, 90});
            System.out.println("Array containing sine values: ");
            NDArray sin = a.mul(Math.PI / 180).sin();
            System.out.println(sin.toDebugString(100, 10, 100, 100));
            System.out.println("Calculating inverse sine of angles, returns values in radians: ");
            NDArray inv = sin.asin();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("Checking the result by converting to degrees: ");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("arccos and arctan functions behave similarly: ");
            NDArray cos = a.mul(Math.PI / 180).cos();
            System.out.println(cos.toDebugString(100, 10, 100, 100));
            System.out.println("Inverse cosine: ");
            inv = cos.acos();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("Units in degrees: ");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("tan function: ");
            NDArray tan = a.mul(Math.PI / 180).tan();
            System.out.println(tan.toDebugString(100, 10, 100, 100));
            System.out.println("Inverse tangent: ");
            inv = tan.atan();
            System.out.println(inv.toDebugString(100, 10, 100, 100));
            System.out.println("Units in degrees: ");
            b = inv.toDegrees();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3. Rounding functions
            // 3.1 Rounding off
            a = manager.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
            System.out.println("Original array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("After rounding off: ");
            b = a.round();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.2 Floor function
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("Array provided: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Modified array: ");
            b = a.floor();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.3 Ceiling function
            a = manager.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
            System.out.println("Array provided: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Modified array: ");
            b = a.ceil();
            System.out.println(b.toDebugString(100, 10, 100, 100));

        }

    }
}
