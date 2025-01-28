package top.aias.examples;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * INDArray Sorting and Conditional Filtering Functions
 * INDArray 排序和条件过滤函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No8SortConditionExample {

    private No8SortConditionExample() {
    }

    public static void main(String[] args) {
        // 1. Array Sorting - numpy.sort()
        // 1. 数组排序 - numpy.sort()
        INDArray a = Nd4j.create(new int[][]{{3, 7}, {9, 1}});
        System.out.println("Our array is: \n" + a);
        // 我们的数组是：
        System.out.println("Calling sort() function: ");
        // 调用sort()函数：
        // 沿行排序（默认轴=1）
        INDArray b = Nd4j.sort(a, 1, true); // Sort along rows (default axis=1)
        System.out.println(b);

        // 沿列排序：
        System.out.println("Sort along columns: ");
        b = Nd4j.sort(a, 0, true);
        System.out.println(b);

        // 2. Indexes of maximum and minimum elements - numpy.argmax() and numpy.argmin()
        // 2. 最大值和最小值元素的索引 - numpy.argmax() 和 numpy.argmin()
        a = Nd4j.create(new int[][]{{30, 40, 70}, {80, 20, 10}, {50, 90, 60}});
        // 我们的数组是：
        System.out.println("Our array is: \n" + a);
        // 调用argmax()函数：
        b = Nd4j.argMax(a);
        System.out.println("Calling argmax() function: \n" + b);
        // 沿行找最大值索引
        b = Nd4j.argMax(a, 1); // Argmax along rows
        System.out.println("Index of the maximum value along rows: \n" + b);
        b = Nd4j.argMax(a, 0); // Argmax along columns
        // 沿列找最大值索引
        System.out.println("Index of the maximum value along columns: \n" + b);
        // 调用argmin()函数：
        b = Nd4j.argMin(a);
        System.out.println("Calling argMin() function: \n" + b);
        // 沿行找最小值索引
        b = Nd4j.argMin(a, 1); // Argmin along rows
        System.out.println("Index of the minimum value along rows: \n" + b);
        b = Nd4j.argMin(a, 0); // Argmin along columns
        // 沿列找最小值索引
        System.out.println("Index of the minimum value along columns: \n" + b);

        // 3. Indexes of non-zero elements in the array - numpy.nonzero()
        // 3. 数组中非零元素的索引 - numpy.nonzero()
        a = Nd4j.create(new int[][]{{30, 40, 0}, {0, 20, 10}, {50, 0, 60}});
        // 我们的数组是：
        System.out.println("Our array is: \n" + a);
        // 获取非零元素的坐标
        System.out.println("Indexes of non-zero elements: ");
        INDArray[] indexes = Nd4j.where(a.neq(0), null, null);
        INDArray pos = Nd4j.hstack(Nd4j.expandDims(indexes[0], 1), Nd4j.expandDims(indexes[1], 1));
        // 提取对应的数据
        int[][] posArr = pos.toIntMatrix();
        double[] values = new double[(int) indexes[0].length()];
        for (int i = 0; i < values.length; i++) {
            values[i] = a.getDouble(posArr[i][0], posArr[i][1]);
        }
        // 打印提取的非零元素
        System.out.println("Non-zero elements: ");
        // 非零元素：
        for (double v : values) {
            System.out.print(v + " ");
        }

        // 4. Indexes of elements in the array satisfying a given condition - numpy.where()
        // 4. 数组中满足给定条件的元素的索引 - numpy.where()
        a = Nd4j.arange(9).reshape(3, 3);
        System.out.println("Our array is: \n" + a);
        // 我们的数组是：
        System.out.println("Indexes of elements greater than 3: ");
        // 大于3的元素的索引：
        INDArray condition = a.gt(3);
        System.out.println(condition);
        System.out.println("Use these indexes to get the elements satisfying the condition: ");
        // 使用这些索引获取满足条件的元素：
        INDArray filtered = a.mul(condition);
        System.out.println(filtered);
    }
}
