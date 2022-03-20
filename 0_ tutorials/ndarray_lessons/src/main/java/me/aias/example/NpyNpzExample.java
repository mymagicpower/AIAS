package me.aias.example;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 读取numpy保存的npz、npy文件
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */

public final class NpyNpzExample {

    private static final Logger logger = LoggerFactory.getLogger(NpyNpzExample.class);

    private NpyNpzExample() {
    }

    public static void main(String[] args) throws Exception {
        String npyDataPath = "src/test/resources/data.npy";
        String npzDataPath = "src/test/resources/data.npz";

    }

    public static NDArray apply(NDManager manager, String npzDataPath, NDArray features)
            throws Exception {
        //https://github.com/deepjavalibrary/djl/blob/master/api/src/test/java/ai/djl/ndarray/NDSerializerTest.java
        //https://github.com/deepjavalibrary/djl/blob/master/api/src/test/java/ai/djl/ndarray/NDListTest.java
        byte[] data = Files.readAllBytes(Paths.get(npzDataPath));
        NDList decoded = NDList.decode(manager, data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length + 1);
        decoded.encode(bos, true);
        NDList list = NDList.decode(manager, bos.toByteArray());
        NDArray meanNDArray = list.get(0);//mean
        meanNDArray = meanNDArray.toType(DataType.FLOAT32, false);
        NDArray stdNDArray = list.get(1);//std
        stdNDArray = stdNDArray.toType(DataType.FLOAT32, false);

        // (features - self._mean) / (self._std + eps)
        return features;
    }
}
