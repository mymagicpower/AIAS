package me.aias.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 读取numpy保存的npz、npy文件
 * Read npz and npy files saved in numpy
 *
 * @author calvin
 * @mail 179209347@qq.com
 */

public final class NpyNpzExample {

    private static final Logger logger = LoggerFactory.getLogger(NpyNpzExample.class);

    private NpyNpzExample() {
    }

    public static void main(String[] args) throws Exception {
        String npyDataPath = "src/test/resources/data.npy";
        String npzDataPath = "src/test/resources/data.npz";

        logger.info("npy reading test: ");
        File file = new File(npyDataPath);
        INDArray array = Nd4j.readNpy(file);
        float[][] npyData = array.toFloatMatrix();
        for (int i = 0; i < npyData.length; i++) {
            logger.info(Arrays.toString(npyData[i]));
        }

        logger.info("npz reading test: ");
        file = new File(npzDataPath);
        Map<String, INDArray> map = Nd4j.createFromNpzFile(file);
        Set<Map.Entry<String, INDArray>> set = map.entrySet();
        Iterator<Map.Entry<String, INDArray>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, INDArray> me = iter.next();
            logger.info("Array name: " + me.getKey());
            array = me.getValue();
            npyData = array.toFloatMatrix();

            for (int i = 0; i < npyData.length; i++) {
                logger.info(Arrays.toString(npyData[i]));
            }
        }


    }
}
