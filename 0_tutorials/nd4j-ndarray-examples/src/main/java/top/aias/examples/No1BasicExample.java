package top.aias.examples;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * INDArray Basic Operations
 *
 * INDArray 基本操作
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class No1BasicExample {

    public static void main(String[] args) {
        // 1. Create Data Object - Vector
        // 1. 创建数据对象 - 向量
        int[] vector = new int[]{1, 2, 3};
        INDArray nd = Nd4j.create(vector);
        System.out.println(nd);

        // 2.1 Create Data Object - Matrix
        // 2.1 创建数据对象 - 矩阵
        int[][] mat = new int[][]{{1, 2}, {3, 4}};
        nd = Nd4j.create(mat);
        System.out.println(nd);

        // 2.2 Create Data Object - Matrix
        // 2.2 创建数据对象 - 矩阵
        int[] arr = new int[]{1, 2, 3, 4};
        nd = Nd4j.create(arr, new long[]{1, 4}, DataType.INT32);
        nd = nd.reshape(2, 2);
        System.out.println(nd);

        // 2.3 Create Data Object - Specify Matrix Dimensions
        // 2.3 创建数据对象 - 指定矩阵维度
        nd = Nd4j.create(new float[]{0.485f, 0.456f, 0.406f}, new long[]{1, 1, 3});
        System.out.println(nd);


        // 3. Data type conversion
        // 3. 数据类型转换
        nd = Nd4j.create(arr, new long[]{1, 4}, DataType.INT32);
        // 将 INDArray 转换为 FLOAT 类型
        INDArray ndFloat = nd.castTo(DataType.FLOAT);
        System.out.println(ndFloat);


        // 4. Modifications to array
        // 4. 数组修改
        nd = Nd4j.zeros(3, 3);
        // Set value at row 0, column 1 to value 1.0
        // 将第 0 行第 1 列的值设置为 1.0
        nd.putScalar(0, 1, 1.0f);
        // Set value at row 2, column 2 to value 3.0
        // 将第 2 行第 2 列的值设置为 3.0
        nd.putScalar(2, 2, 3.0f);
        System.out.println("\nArray after Set operations:");
        System.out.println(nd);

        // 5. Get individual value
        // 5. 获取单个值
        float val = nd.getFloat(0, 1);
        System.out.println("\nValue at (0,1):     " + val);

        // 6. creates a new copy and separate array
        // 6. 创建一个新的副本和独立数组
        nd = Nd4j.zeros(3, 3);
        System.out.println("originalArray:\n" + nd);
        INDArray newCopy = nd.dup();
        newCopy.addi(100);
        System.out.println("After .addi(100):\n" + newCopy);
        System.out.println("originalArray:\n" + nd);
    }

}
