package top.aias.trans.model;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 编码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class EncoderPool {
    private int poolSize;
    private ZooModel<NDList, NDList> model;
    private ArrayList<Predictor<int[], NDArray>> predictorList = new ArrayList<>();


    public EncoderPool(ZooModel<NDList, NDList> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<int[], NDArray> predictor = model.newPredictor(new EncoderTranslator());
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<int[], NDArray> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<int[], NDArray> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<int[], NDArray> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<int[], NDArray> predictor : predictorList) {
            predictor.close();
        }

    }
}