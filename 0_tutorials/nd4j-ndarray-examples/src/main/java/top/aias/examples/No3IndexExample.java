package top.aias.examples;


import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

/**
 * NDarray Indexing and Slicing
 * NDarray 索引和切片
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No3IndexExample {

    private No3IndexExample() {
    }

    public static void main(String[] args) {
        // 1. Accessing and modifying the contents of an ndarray object using indexing or slicing.
        // 1. 使用索引或切片访问和修改ndarray对象的内容。
        INDArray a = Nd4j.arange(10);
        INDArray b = a.get(NDArrayIndex.interval(2, 2, 7)); // Slice with step 2
        // 使用步长为2的切片
        System.out.println("1. Slice a[2:7:2]:\n" + b);

        // 2. Usage of the colon ":".
        // 2. 使用冒号 ":"。
        // Example 2.1
        // 示例 2.1
        a = Nd4j.arange(10);
        b = a.get(NDArrayIndex.point(5)); // Access a[5]
        // 访问a[5]
        System.out.println("2.1 Element a[5]:\n" + b);

        // Example 2.2
        // 示例 2.2
        a = Nd4j.arange(10);
        b = a.get(NDArrayIndex.interval(2, a.length())); // Slice a[2:]
        // 切片 a[2:]
        System.out.println("2.2 Slice a[2:]:\n" + b);

        // Example 2.3
        // 示例 2.3
        a = Nd4j.arange(10);
        b = a.get(NDArrayIndex.interval(2, 5)); // Slice a[2:5]
        // 切片 a[2:5]
        System.out.println("2.3 Slice a[2:5]:\n" + b);

        // 3.1 Multi-dimensional array indexing.
        // 3.1 多维数组索引。
        a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        System.out.println("3.1 Original 2D array:\n" + a);
        b = a.get(NDArrayIndex.interval(1, a.rows()), NDArrayIndex.all()); // Slice a[1:] or Slice a[1:,:]
        // 切片 a[1:] 或 a[1,:]
        System.out.println("3.1 Slice a[1:]:\n " + b);
        // The output result may lack the last row or the last column, it's just a printing issue, the data itself is correct.
        //  输出结果会缺最后一行，或者最后一列，只是打印输出问题，数据本身是正确的。
        //  [[         3,         4,         5],
        // []]
        INDArray flattened = Nd4j.toFlattened(b);
        System.out.println("3.1 Slice a[1:]: toFlattened\n " + flattened);


        // 3.2 Multi-dimensional array indexing, ellipsis ... (or colon:).
        // 3.2 多维数组索引，省略号 ... （或冒号 :）。
        a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        System.out.println("3.2 Original 2D array:\n" + a);
        System.out.println("Elements from the 2nd column: ");
        b = a.getColumn(1); // Slice a[:,1]
        // b = a.get(NDArrayIndex.all(), NDArrayIndex.point(1)); // Slice a[:,1]
        System.out.println(b);
        System.out.println("Elements from the 2nd row: ");
        b = a.getRow(1); // Slice a[1,:]
        // b = a.get(NDArrayIndex.point(1), NDArrayIndex.all()); // Slice a[1,:]
        System.out.println(b);
        System.out.println("Elements from the 2nd column and all remaining elements: ");
        b = a.get(NDArrayIndex.all(), NDArrayIndex.interval(1, a.columns())); // Slice a[:,1:]
        System.out.println(b);
        // The output result may lack the last row or the last column, it's just a printing issue, the data itself is correct.
        //  输出结果会缺最后一行，或者最后一列，只是打印输出问题，数据本身是正确的。
        // [[         2,         3],
        //  [         4,         5],
        // []]
        flattened = Nd4j.toFlattened(b);
        System.out.println("toFlattened\n " + flattened);

        // 4. Boolean indexing.
        // 4. 布尔索引。
        INDArray x = Nd4j.create(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}});
        System.out.println("Our array is: ");
        // 我们的数组是：
        System.out.println(x);
        System.out.println("Elements greater than 5 are: ");
        // 大于5的元素是：
        //        INDArray y = x.get(x.gt(5)); // Extract elements > 5
        INDArray mask = x.gt(5);
        INDArray y = Nd4j.toFlattened(x.mul(mask)); // Use element-wise multiplication with mask
        // 使用带掩码的逐元素乘法
        System.out.println(y);

        // 5. Setting value
        // 5. 设置值
        // 5.1 Set the 3rd column (index 2) to zeros:
        // 5.1 将第三列（索引2）设置为零：
        a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        INDArray zerosColumn = Nd4j.zeros(3, 1);
        a.put(new INDArrayIndex[]{NDArrayIndex.all(), NDArrayIndex.point(2)}, zerosColumn);
        System.out.println("\n\n\nAfter setting operation:\n" + a);

        // 5.2 Set the 2,3rd column (index 1,2) to zeros:
        // 5.2 将第二、三列（索引1,2）设置为零：
        a = Nd4j.create(new int[][]{{1, 2, 3}, {3, 4, 5}, {4, 5, 6}});
        INDArray zerosColumns = Nd4j.zeros(3, 2);
        a.put(new INDArrayIndex[]{NDArrayIndex.all(), NDArrayIndex.interval(1, 3)}, zerosColumns);
        System.out.println("\n\n\nAfter setting operation:\n" + a);
    }
}
