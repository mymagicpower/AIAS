package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;

/**
 * NDarray Indexing and Slicing
 * NDarray 索引和切片
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No3IndexAndSettingExample {

    private No3IndexAndSettingExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. Accessing and modifying the contents of an ndarray object using indexing or slicing.
            // 1. 使用索引或切片访问和修改NDArray对象的内容。
            NDArray a = manager.arange(10);
            NDArray b = a.get("2:7:2");
            System.out.println("1. Slice a[2:7:2]:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 2. Usage of the colon ":".
            // 2. 冒号 ":" 的用法。
            // Example 2.1
            // 示例 2.1
            a = manager.arange(10);
            b = a.get("5");
            System.out.println("2.1 Element a[5]:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            // Example 2.2
            // 示例 2.2
            a = manager.arange(10);
            b = a.get("2:");
            System.out.println("2.2 Slice a[2:]:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            // Example 2.3
            // 示例 2.3
            a = manager.arange(10);
            b = a.get("2:5");
            System.out.println("2.3 Slice a[2:5]:\n");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3.1 Multi-dimensional array indexing.
            // 3.1 多维数组索引。
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Slicing from the index a[1:]:");
            // 从索引 a[1:] 开始切片：
            b = a.get("1:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 3.2 Multi-dimensional array indexing
            // 3.2 多维数组索引
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
            System.out.println("Elements from the 2nd column: ");
            // 来自第二列的元素：
            b = a.get(":,1");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Elements from the 2nd row: ");
            // 来自第二行的元素：
            b = a.get("1,:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));
            System.out.println("Elements from the 2nd column and all remaining elements: ");
            // 第二列及所有剩余元素：
            b = a.get(":,1:");
            System.out.println(b.toDebugString(100, 10, 100, 100, true));

            // 4. Boolean indexing.
            // 4. 布尔索引。
            NDArray x = manager.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
            System.out.println("Our array is: ");
            // 我们的数组是：
            System.out.println(x.toDebugString(100, 10, 100, 100, true));
            System.out.println("Elements greater than 5 are: ");
            // 大于5的元素是：
            NDArray y = x.get(x.gt(5));
            System.out.println(y.toDebugString(100, 10, 100, 100, true));

            // 5. Setting value
            // 5. 设置值
            // 5.1 Set the 3rd column (index 2) to zeros:
            // 5.1 将第3列（索引2）设置为零：
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            NDArray zerosColumn = manager.zeros(new Shape(3));
            a.set(new NDIndex(":,2"), zerosColumn);
            System.out.println("\nAfter setting operation:");
            // 设置操作后：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));

            // 5.2 Set the 2,3rd column (index 1,2) to zeros:
            // 5.2 将第2,3列（索引1,2）设置为零：
            a = manager.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
            NDArray zerosColumns = manager.zeros(new Shape(3, 2));
            a.set(new NDIndex(":,1:3"), zerosColumns);
            System.out.println("\nAfter setting operation:");
            // 设置操作后：
            System.out.println(a.toDebugString(100, 10, 100, 100, true));
        }
    }
}
