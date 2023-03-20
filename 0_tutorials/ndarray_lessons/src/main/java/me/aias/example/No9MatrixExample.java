package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * NDarray Matrix
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No9MatrixExample {

    private No9MatrixExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Transpose Matrix
            NDArray a = manager.arange(12).reshape(3, 4);
            System.out.println("Original Array: ");
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Transposed Array: ");
            NDArray b = a.transpose();
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. Create a matrix filled with 0 - zeros()
            a = manager.zeros(new Shape(2, 2));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 3. Create a matrix filled with 1  - ones()
            a = manager.ones(new Shape(2, 2));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 4. Return a matrix with diagonal elements as 1 and others as 0 - eye()
            a = manager.eye(3, 4, 0, DataType.INT32);
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 5. Create a matrix of given size filled with random data - rand()
            a = manager.randomUniform(0, 1, new Shape(3, 3));
            System.out.println(a.toDebugString(100, 10, 100, 100));

            // 6. Dot product - dot()
            a = manager.create(new int[][]{{1, 2}, {3, 4}});
            b = manager.create(new int[][]{{11, 12}, {13, 14}});
            NDArray c = a.dot(b);
            // The calculation is:
            // [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
            System.out.println(c.toDebugString(100, 10, 100, 100));

            // 7. Matrix multiplication - matMul()
            a = manager.create(new int[][]{{1, 0}, {0, 1}});
            b = manager.create(new int[][]{{4, 1}, {2, 2}});
            c = a.matMul(b);
            System.out.println(c.toDebugString(100, 10, 100, 100));
        }
    }
}
