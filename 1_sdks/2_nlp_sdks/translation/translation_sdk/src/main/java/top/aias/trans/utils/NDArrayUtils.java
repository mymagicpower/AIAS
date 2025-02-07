package top.aias.trans.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
/**
 * NDArray 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class NDArrayUtils {

    private NDArrayUtils() {
    }

    public static NDArray expand(NDArray array, long beam) {
        NDList list = new NDList();
        for (long i = 0; i < beam; i++) {
            list.add(array);
        }
        NDArray result = NDArrays.concat(list, 0);

        return result;
    }
}