package top.aias.examples;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * INDArray Statistical Functions
 * INDArray 统计函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No7StatisticExample {

    private No7StatisticExample() {
    }

    public static void main(String[] args) {
        // 1. Minimum and Maximum Elements
        // 1. 最小值和最大值元素
        INDArray a = Nd4j.create(new int[][]{{3, 7, 5}, {8, 4, 3}, {2, 4, 9}});
        System.out.println("Our array is: ");
        // 我们的数组是：
        System.out.println(a);

        System.out.println("Calling min() function along axis 1: ");
        // 沿轴1调用min()函数：
        INDArray b = a.min(1);
        System.out.println(b);

        System.out.println("Calling min() function along axis 0: ");
        // 沿轴0调用min()函数：
        b = a.min(0);
        System.out.println(b);

        System.out.println("Calling max() function: ");
        // 调用max()函数：
        b = a.max();
        System.out.println(b);

        System.out.println("Calling max() function along axis 0: ");
        // 沿轴0调用max()函数：
        b = a.max(0);
        System.out.println(b);

        // 2. Arithmetic Mean
        // 2. 算术平均数
        a = Nd4j.create(new float[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        System.out.println("Our array is: ");
        // 我们的数组是：
        System.out.println(a);

        System.out.println("Calling mean() function: ");
        // 调用mean()函数：
        b = a.mean();
        System.out.println(b);

        System.out.println("Calling mean() function along axis 0: ");
        // 沿轴0调用mean()函数：
        b = a.mean(0);
        System.out.println(b);

        System.out.println("Calling mean() function along axis 1: ");
        // 沿轴1调用mean()函数：
        b = a.mean(1);
        System.out.println(b);

        // 3. Standard Deviation - std = sqrt(mean((x - x.mean())**2))
        // 3. 标准差 - std = sqrt(mean((x - x.mean())**2))
        a = Nd4j.create(new float[]{1, 2, 3, 4});
        a = Nd4j.create(new float[]{1, 2, 3, 4});
        b = Transforms.sqrt(a.sub(a.mean()).mul(a.sub(a.mean())).mean());
        System.out.println("Standard deviation: ");
        // 标准差：
        System.out.println(b);

        // 4. Variance - mean((x - x.mean())**2)
        // 4. 方差 - mean((x - x.mean())**2)
        a = Nd4j.create(new float[]{1, 2, 3, 4});
        b = a.sub(a.mean()).mul(a.sub(a.mean())).mean();
        System.out.println("Variance: ");
        // 方差：
        System.out.println(b);
    }
}
