package top.aias.examples;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * NDarray Broadcast
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No4BroadcastExample {

    private No4BroadcastExample() {
    }

    public static void main(String[] args) {
        // 1. Two arrays a and b have the same shape.
        // 1. 两个数组a和b具有相同的形状。
        // 注意 Nd4j.create(new int[]{1, 2, 3, 4})，此时，int数组是shape，不是数据。
        INDArray x = Nd4j.create(new int[]{1, 2, 3, 4}, new long[]{1, 4}, DataType.INT32);
        INDArray y = Nd4j.create(new int[]{10, 20, 30, 40}, new long[]{1, 4}, DataType.INT32);
        INDArray z = x.mul(y);
        System.out.println("Result of element-wise multiplication:");
        // 元素级乘法的结果：
        System.out.println(z);

        // 2. Broadcasting mechanism is automatically triggered when the shapes of the two arrays in an operation are different.
        // 2. 当操作中两个数组的形状不同时，会自动触发广播机制。
        x = Nd4j.create(new int[][]{
                {0, 0, 0},
                {10, 10, 10},
                {20, 20, 20},
                {30, 30, 30}
        });
        y = Nd4j.create(new int[]{1, 2, 3}, new long[]{1, 3}, DataType.INT32);
        z = x.addRowVector(y); // Use addRowVector to add a 1D array to each row.
        // 使用 addRowVector 将一个1D数组加到每一行。
        System.out.println("Result of broadcasting addition:");
        // 广播加法的结果：
        System.out.println(z);
    }
}
