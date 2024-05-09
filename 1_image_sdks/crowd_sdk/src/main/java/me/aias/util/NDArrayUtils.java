package me.aias.util;

import ai.djl.ndarray.NDArray;
/**
 * NDArray Utils
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class NDArrayUtils {

    /**
     * float NDArray To float[][] Array
     * @param ndArray
     * @return
     */
    public static float[][] floatNDArrayToArray(NDArray ndArray) {
        int rows = (int) (ndArray.getShape().get(0));
        int cols = (int) (ndArray.getShape().get(1));
        float[][] arr = new float[rows][cols];

        float[] arrs = ndArray.toFloatArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = arrs[i * cols + j];
            }
        }
        return arr;
    }
}
