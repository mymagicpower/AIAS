package top.aias.examples;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * INDArray Matrix Example
 * INDArray 矩阵示例
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No9MatrixExample {

    private No9MatrixExample() {
    }

    public static void main(String[] args) {
        // 1. Transpose Matrix
        // 1. 转置矩阵
        INDArray a = Nd4j.arange(12).reshape(3, 4);
        System.out.println("Original Array: ");
        // 原始数组：
        System.out.println(a);
        System.out.println("Transposed Array: ");
        // 转置后的数组：
        INDArray b = a.transpose();
        System.out.println(b);

        // 2. Create a matrix filled with 0 - zeros()
        // 2. 创建一个填充为0的矩阵 - zeros()
        a = Nd4j.zeros(2, 2);
        System.out.println("Matrix filled with zeros:");
        // 填充为零的矩阵：
        System.out.println(a);

        // 3. Create a matrix filled with 1 - ones()
        // 3. 创建一个填充为1的矩阵 - ones()
        a = Nd4j.ones(2, 2);
        System.out.println("Matrix filled with ones:");
        // 填充为1的矩阵：
        System.out.println(a);

        // 4. Return a matrix with diagonal elements as 1 and others as 0 - eye()
        // 4. 返回一个对角线元素为1，其他元素为0的矩阵 - eye()
        int n = 3;
        int m = 4;
        INDArray matrix = Nd4j.eye(n); // Create an identity matrix of size n x n
        // 创建一个n x n的单位矩阵
        if (m > n) {
            // Expand the matrix horizontally to match columns
            // 水平扩展矩阵以匹配列数
            matrix = Nd4j.hstack(matrix, Nd4j.zeros(n, m - n));
        } else if (m < n) {
            // Slice the matrix to reduce columns
            // 切割矩阵以减少列数
            matrix = matrix.getColumns(0, m - 1);
        }
        System.out.println("Identity-like matrix:");
        // 类单位矩阵：
        System.out.println(matrix);

        // 5. Create a matrix of given size filled with random data - rand()
        // 5. 创建一个指定大小并填充随机数据的矩阵 - rand()
        a = Nd4j.rand(3, 3);
        System.out.println("Random matrix:");
        // 随机矩阵：
        System.out.println(a);

        // 6. Dot product - dot()
        // 6. 点积 - dot()
        a = Nd4j.create(new int[][]{{1, 2}, {3, 4}});
        b = Nd4j.create(new int[][]{{11, 12}, {13, 14}});
        INDArray c = a.mmul(b);  // Equivalent to dot product for matrices
        // 相当于矩阵的点积
        // The calculation is:
        // [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
        // 计算过程：
        // [[1*11+2*13, 1*12+2*14],[3*11+4*13, 3*12+4*14]]
        System.out.println("Dot product result:");
        // 点积结果：
        System.out.println(c);

        // 7. Matrix multiplication - mmul()
        // 7.1 二维矩阵乘法 - mmul()
        a = Nd4j.create(new int[][]{{1, 0}, {0, 1}});
        b = Nd4j.create(new int[][]{{4, 1}, {2, 2}});
        c = a.mmul(b);
        System.out.println("Matrix multiplication result:");
        // 矩阵乘法结果：
        System.out.println(c);

        // 7.2 Three-dimensional matrix multiplication - mmul()
        // 7.2 三维矩阵乘法 - mmul()
        // Define two 3D arrays A and B
        // 定义两个三维数组 A 和 B
        INDArray A = Nd4j.create(new double[][][]{
                {
                        {1, 2, 3},  // 第一个批次矩阵 1 (2x3)
                        {4, 5, 6}   // First batch matrix 1 (2x3)
                },
                {
                        {2, 3, 4},  // 第二个批次矩阵 2 (2x3)
                        {5, 6, 7}   // Second batch matrix 2 (2x3)
                }
        });

        INDArray B = Nd4j.create(new double[][][]{
                {
                        {1, 2},  // 第一个批次矩阵 1 (3x2)
                        {3, 4},  // First batch matrix 1 (3x2)
                        {5, 6}
                },
                {
                        {2, 3},  // 第二个批次矩阵 2 (3x2)
                        {4, 5},  // Second batch matrix 2 (3x2)
                        {6, 7}
                }
        });

        // Perform matrix multiplication
        // 执行矩阵乘法
        INDArray C = Nd4j.matmul(A, B);

        // Print the shapes and results
        // 输出形状和结果
        System.out.println("Shape of A: " + A.shapeInfoToString());  // (2, 2, 3)
        System.out.println("Shape of B: " + B.shapeInfoToString());  // (2, 3, 2)
        System.out.println("Shape of C: " + C.shapeInfoToString());  // (2, 2, 2)
        System.out.println("Result C:\n" + C);
    }
}
