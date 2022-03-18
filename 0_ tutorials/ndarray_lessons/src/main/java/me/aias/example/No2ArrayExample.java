package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

/**
 * Ndarray 数组操作
 * http://aias.top/
 *
 * @author Calvin
 */

public final class No2ArrayExample {

    private No2ArrayExample() {
    }

    public static void main(String[] args) {
        try (NDManager manager = NDManager.newBaseManager()) {
            // 1. 数组的维数
            NDArray nd = manager.arange(24); // 现只有一个维度
            System.out.println(nd.getShape().dimension());
            // 现在调整其大小
            nd = nd.reshape(2, 4, 3); // 现在拥有三个维度
            System.out.println(nd.getShape().dimension());

            // 2. 数组的shape
            nd = manager.create(new int[][]{{1, 2, 3}, {4, 5, 6}});
            System.out.println(nd.getShape());

            // 3. 调整数组形状
            nd = nd.reshape(3, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 4. 创建数组 zeros
            nd = manager.zeros(new Shape(5), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 5. 创建数组 ones
            nd = manager.ones(new Shape(2, 2), DataType.INT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6. 从数值范围创建数组
            // 6.1 生成 0 到 5 的数组
            nd = manager.arange(5);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.2 设置返回类型为 float
            nd = manager.arange(0, 5, 1, DataType.FLOAT32);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.3 设置了起始值、终止值及步长
            nd = manager.arange(10, 20, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 6.4 等差数列 linspace
            nd = manager.linspace(1, 10, 10);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7. 数组操作
            // 7.1 生成 0 到 5 的数组
            nd = manager.arange(12).reshape(3, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.transpose();
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.2 交换数组的两个轴
            nd = manager.arange(8).reshape(2, 2, 2);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.swapAxes(2, 0);
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.3 广播 - 将数组广播到新形状
            nd = manager.arange(4).reshape(1, 4);
            System.out.println(nd.toDebugString(100, 10, 100, 100));
            nd = nd.broadcast(new Shape(4, 4));
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.4 在指定位置插入新的轴来扩展数组形状
            NDArray x = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("数组 x：");
            System.out.println(x.toDebugString(100, 10, 100, 100));
            // 在位置 0 插入轴
            NDArray y = x.expandDims(0);
            System.out.println("数组 y：");
            System.out.println(y.toDebugString(100, 10, 100, 100));
            System.out.println("数组 x 和 y 的形状：");
            System.out.println(x.getShape() + " " + y.getShape());
            // 在位置 1 插入轴
            y = x.expandDims(1);
            System.out.println("在位置 1 插入轴之后的数组 y：");
            System.out.println(y.toDebugString(100, 10, 100, 100));

            System.out.println("x.ndim 和 y.ndim：");
            System.out.println(x.getShape().dimension() + " " + y.getShape().dimension());

            System.out.println("数组 x 和 y 的形状：");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.5 从给定数组的形状中删除一维的条目 squeeze
            x = manager.arange(9).reshape(1, 3, 3);
            System.out.println("数组 x：");
            System.out.println(x.toDebugString(100, 10, 100, 100));

            y = x.squeeze();
            System.out.println("数组 y：");
            System.out.println(y.toDebugString(100, 10, 100, 100));

            System.out.println("数组 x 和 y 的形状：");
            System.out.println(x.getShape() + " " + y.getShape());

            // 7.6 连接数组 concatenate
            NDArray a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("第一个数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            NDArray b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("第二个数组：");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            nd = NDArrays.concat(new NDList(a, b));
            System.out.println("沿轴 0 连接两个数组：");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            nd = NDArrays.concat(new NDList(a, b), 1);
            System.out.println("沿轴 1 连接两个数组：");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.7 沿新轴堆叠数组序列 stack
            a = manager.create(new int[][]{{1, 2}, {3, 4}});
            System.out.println("第一个数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            b = manager.create(new int[][]{{5, 6}, {7, 8}});
            System.out.println("第二个数组：");
            System.out.println(b.toDebugString(100, 10, 100, 100));

            nd = NDArrays.stack(new NDList(a, b));
            System.out.println("沿轴 0 堆叠两个数组：");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            nd = NDArrays.stack(new NDList(a, b), 1);
            System.out.println("沿轴 1 堆叠两个数组：");
            System.out.println(nd.toDebugString(100, 10, 100, 100));

            // 7.8 沿特定的轴将数组分割为子数组 numpy.split
            a = manager.arange(9);
            System.out.println("第一个数组：");
            System.out.println(a.toDebugString(100, 10, 100, 100));

            NDList list = a.split(3);
            System.out.println("将数组分为三个大小相等的子数组：");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

            list = a.split(new long[]{4, 7});
            System.out.println("将数组在一维数组中表明的位置分割：");
            System.out.println(list.get(0).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(1).toDebugString(100, 10, 100, 100));
            System.out.println(list.get(2).toDebugString(100, 10, 100, 100));

        }

    }
}
