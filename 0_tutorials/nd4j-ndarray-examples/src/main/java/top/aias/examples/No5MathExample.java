package top.aias.examples;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * INDArray Mathematical Functions
 * INDArray 数学函数
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public final class No5MathExample {

    private No5MathExample() {
    }

    public static void main(String[] args) {
        // 1. Trigonometric functions
        // 1. 三角函数
        INDArray a = Nd4j.create(new double[]{0, 30, 45, 60, 90});
        System.out.println("Sine values for different angles: ");
        // 不同角度的正弦值：
        INDArray b = Transforms.sin(a.mul(Math.PI / 180));
        System.out.println(b);

        System.out.println("Cosine values for angles in the array: ");
        // 数组中角度的余弦值：
        b = Transforms.cos(a.mul(Math.PI / 180));
        System.out.println(b);

        System.out.println("Tangent values for angles in the array: ");
        // 数组中角度的正切值：
        b = Transforms.tan(a.mul(Math.PI / 180));
        System.out.println(b);

        // 2. Inverse trigonometric functions
        // 2. 反三角函数
        System.out.println("Array containing sine values: ");
        // 包含正弦值的数组：
        INDArray sin = Transforms.sin(a.mul(Math.PI / 180));
        System.out.println(sin);

        System.out.println("Calculating inverse sine of angles, returns values in radians: ");
        // 计算角度的反正弦，返回弧度值：
        INDArray inv = Transforms.asin(sin);
        System.out.println(inv);

        System.out.println("Checking the result by converting to degrees: ");
        // 通过转换为度来检查结果：
        b = inv.mul(180 / Math.PI);
        System.out.println(b);

        System.out.println("arccos and arctan functions behave similarly: ");
        // 反余弦和反正切函数行为相似：
        INDArray cos = Transforms.cos(a.mul(Math.PI / 180));
        System.out.println(cos);

        System.out.println("Inverse cosine: ");
        // 反余弦：
        inv = Transforms.acos(cos);
        System.out.println(inv);

        System.out.println("Units in degrees: ");
        // 单位为度：
        b = inv.mul(180 / Math.PI);
        System.out.println(b);

        System.out.println("tan function: ");
        // 正切函数：
        INDArray tan = Transforms.tan(a.mul(Math.PI / 180));
        System.out.println(tan);

        System.out.println("Inverse tangent: ");
        // 反正切：
        inv = Transforms.atan(tan);
        System.out.println(inv);

        System.out.println("Units in degrees: ");
        // 单位为度：
        b = inv.mul(180 / Math.PI);
        System.out.println(b);

        // 3. Rounding functions
        // 3. 四舍五入函数
        // 3.1 Rounding off
        // 3.1 四舍五入
        a = Nd4j.create(new double[]{1.0, 5.55, 123, 0.567, 25.532});
        System.out.println("Original array: ");
        // 原始数组：
        System.out.println(a);

        System.out.println("After rounding off: ");
        // 四舍五入后的数组：
        b = Transforms.round(a);
        System.out.println(b);

        // 3.2 Floor function
        // 3.2 向下取整函数
        a = Nd4j.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
        System.out.println("Array provided: ");
        // 提供的数组：
        System.out.println(a);

        System.out.println("Modified array (floor): ");
        // 修改后的数组（向下取整）：
        b = Transforms.floor(a);
        System.out.println(b);

        // 3.3 Ceiling function
        // 3.3 向上取整函数
        a = Nd4j.create(new double[]{-1.7, 1.5, -0.2, 0.6, 10});
        System.out.println("Array provided: ");
        // 提供的数组：
        System.out.println(a);

        System.out.println("Modified array (ceiling): ");
        // 修改后的数组（向上取整）：
        b = Transforms.ceil(a);
        System.out.println(b);
    }
}
