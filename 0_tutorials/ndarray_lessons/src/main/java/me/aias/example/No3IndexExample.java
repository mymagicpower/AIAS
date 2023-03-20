package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;

/**
 * NDarray Indexing and Slicing
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No3IndexExample {

    private No3IndexExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Accessing and modifying the contents of an ndarray object using indexing or slicing.
            NDArray a = manager.arange(10);
            NDArray b = a.get("2:7:2");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 2. Usage of the colon ":".
            // Example 2.1
            a = manager.arange(10);
            b = a.get("5");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // Example 2.2
            a = manager.arange(10);
            b = a.get("2:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // Example 2.3
            a = manager.arange(10);
            b = a.get("2:5");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.1 Multi-dimensional array indexing.
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Slicing from the index a[1:]:");
            b = a.get("1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 3.2 Multi-dimensional array indexing, ellipsis ... (or colon:).
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100));
            System.out.println("Elements from the 2nd column: ");
            b = a.get("...,1");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Elements from the 2nd row: ");
            b = a.get("1,...");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Elements from the 2nd column and all remaining elements: ");
            b = a.get("...,1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            // Colon:
            System.out.println("Elements from the 2nd column: ");
            b = a.get(":,1");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Elements from the 2nd row: ");
            b = a.get("1,:");
            System.out.println(b.toDebugString(100, 10, 100, 100));
            System.out.println("Elements from the 2nd column and all remaining elements: ");
            b = a.get(":,1:");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            // 4. Boolean indexing.
            NDArray x = manager.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
            System.out.println("Our array is: ");
            System.out.println(x.toDebugString(100, 10, 100, 100));
            System.out.println("Elements greater than 5 are: ");
            NDArray y = x.get(x.gt(5));
            System.out.println(y.toDebugString(100, 10, 100, 100));



        }

    }
}
