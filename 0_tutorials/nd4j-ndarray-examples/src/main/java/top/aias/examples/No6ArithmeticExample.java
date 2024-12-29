package top.aias.examples;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * NDarray Arithmetic Functions
 * NDarray 算术函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No6ArithmeticExample {

    private No6ArithmeticExample() {
    }

    public static void main(String[] args) {
        // 1. Addition, subtraction, multiplication, and division
        // 1. 加法、减法、乘法和除法
        INDArray a = Nd4j.arange(0, 9).reshape(3, 3);
        System.out.println("First array: ");
        // 第一个数组：
        System.out.println(a);

        INDArray b = Nd4j.create(new double[]{10, 10, 10});
        System.out.println("Second array: ");
        // 第二个数组：
        System.out.println(b);

        System.out.println("Addition of two arrays: ");
        // 两个数组的加法：
        INDArray c = a.add(b);
        System.out.println(c);

        System.out.println("Subtraction of two arrays: ");
        // 两个数组的减法：
        c = a.sub(b);
        System.out.println(c);

        System.out.println("Multiplication of two arrays: ");
        // 两个数组的乘法：
        c = a.mul(b);
        System.out.println(c);

        System.out.println("Division of two arrays: ");
        // 两个数组的除法：
        c = a.div(b);
        System.out.println(c);

        // 2. Power function
        // 2. 幂运算函数
        a = Nd4j.create(new double[]{10, 20, 30});
        System.out.println("First array: ");
        // 第一个数组：
        System.out.println(a);

        b = Nd4j.create(new double[]{3, 5, 7});
        System.out.println("Second array: ");
        // 第二个数组：
        System.out.println(b);

        System.out.println("Calculating the remainder - mod: ");
        // 计算余数 - 取模：
        c = a.sub(Transforms.floor(a.div(b)).mul(b));
        System.out.println(c);

    }
}
