package top.aias.examples;

import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Ndarray IO - npy/npz
 * Ndarray 输入输出 - npy/npz
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */

public final class No10IOExample {

    private No10IOExample() {
    }

    public static void main(String[] args) throws Exception {
        String npyDataPath = "src/test/resources/outfile.npy";
        String npzDataPath = "src/test/resources/data.npz";

        // 1. Save an array to a binary file in NumPy .npy format.
        // 1. 将数组保存为NumPy .npy格式的二进制文件。
        INDArray a = Nd4j.create(new int[]{1, 2, 3, 4, 5}, new long[]{1, 5}, DataType.INT32);
        File file = new File(npyDataPath);
        Nd4j.writeAsNumpy(a, file);

        // 2. Load arrays or pickled objects from .npy files.
        // 2. 从 .npy 文件加载数组或序列化对象。
        INDArray loadedArray = Nd4j.readNpy(file);
        System.out.println("Loaded array:\n" + loadedArray);

        // 3. Load data from .npz file.
        // 3. 从 .npz 文件加载数据。
        file = new File(npzDataPath);
        Map<String, INDArray> map = Nd4j.createFromNpzFile(file);
        Set<Map.Entry<String, INDArray>> set = map.entrySet();
        Iterator<Map.Entry<String, INDArray>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, INDArray> me = iter.next();
            System.out.println("Array Name: "+ me.getKey());
            loadedArray = me.getValue();
            System.out.println("Loaded array:\n" + loadedArray);
        }
    }
}
