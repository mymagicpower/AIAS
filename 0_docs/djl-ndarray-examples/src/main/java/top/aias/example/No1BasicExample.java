package top.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * Ndarray Basic Operations
 * Ndarray基础操作
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
            // 1. 创建数据对象 - 向量
            int[] vector = new int[]{1, 2, 3};
            NDArray nd = manager.create(vector);
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));

            // 2.1 Create Data Object - Matrix
            // 2.1 创建数据对象 - 矩阵
            int[][] mat = new int[][]{{1, 2}, {3, 4}};
            nd = manager.create(mat);
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));

            // 2.2 Create Data Object - Matrix
            // 2.2 创建数据对象 - 矩阵
            int[] arr = new int[]{1, 2, 3, 4};
            nd = manager.create(arr).reshape(2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));

            // 2.3 Create Data Object - Specify Matrix Dimensions
            // 2.3 创建数据对象 - 指定矩阵维度
            nd = manager.create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(1, 1, 3));
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));


            // 3. Data type conversion
            // 3. 数据类型转换
            nd = manager.create(new int[]{1, 2, 3, 4}).toType(DataType.FLOAT32, false);
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));


            // 4. Modifications to array
            // 4. 修改数组
            // 创建一个3x3的零矩阵
            nd = manager.zeros(new Shape(3, 3));
            // Set value at row 0, column 1 to value 1.0
            // 在(0, 1)位置设置值为1.0
            nd.set(new NDIndex(0, 1), 1.0f);
            // Set value at row 2, column 3 to value 3.0
            // 在(2, 2)位置设置值为3.0
            nd.set(new NDIndex(2, 2), 3.0f);
            System.out.println("\nArray after Set operations:");
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));

            // 5. Get individual value
            // 5. 获取单个值
            // 获取(0, 1)位置的值
            float val = nd.getFloat(0, 1);
            System.out.println("\nValue at (0,1): " + val);

            // 6. creates a new copy and separate array
            // 6. 创建新的副本并分离数组
            nd = manager.zeros(new Shape(3, 3));
            System.out.println("originalArray:");
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));

            NDArray newCopy = nd.duplicate();
            newCopy.addi(100);
            System.out.println("After .addi(100):");
            System.out.println(newCopy.toDebugString(100, 10, 100, 100,true));
            System.out.println("originalArray:");
            System.out.println(nd.toDebugString(100, 10, 100, 100,true));
        }
    }
}
