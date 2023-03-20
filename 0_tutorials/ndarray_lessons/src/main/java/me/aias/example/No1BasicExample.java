package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * Ndarray Basic Operations
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No1BasicExample {

    private No1BasicExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Create Data Object - Vector
            int[] vector = new int[]{1, 2, 3};
            NDArray nd = manager.create(vector);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.1 Create Data Object - Matrix
            int[][] mat = new int[][]{{1, 2}, {3, 4}};
            nd = manager.create(mat);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.2 Create Data Object - Matrix
            int[] arr = new int[]{1, 2, 3, 4};
            nd = manager.create(arr).reshape(2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 2.3 Create Data Object - Specify Matrix Dimensions
            nd = manager.create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
            System.out.println(nd.toDebugString(100, 10, 100, 100));


            // 3. Data tye conversion
            nd = manager.create(new int[]{1, 2, 3, 4}).toType(DataType.FLOAT32, false);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

        }

    }
}
