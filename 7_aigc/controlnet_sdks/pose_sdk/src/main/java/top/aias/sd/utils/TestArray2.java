
package top.aias.sd.utils;

import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;

import java.io.IOException;

public class TestArray2 {
    private static final Engine engine = Engine.getEngine("PyTorch");
    private static final NDManager manager =
            NDManager.newBaseManager(engine.defaultDevice(), engine.getEngineName());

    private TestArray2() {
    }


    public static void main(String[] args) throws IOException, TranslateException, ModelException {

        NDArray a = manager.create(new float[]{1f,2f});
        NDArray sss = a.argSort(0,false);
        long[] index = sss.toLongArray();
        System.out.println(sss.toLongArray());
    }


}