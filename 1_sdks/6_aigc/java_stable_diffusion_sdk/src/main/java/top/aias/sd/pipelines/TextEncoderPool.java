package top.aias.sd.pipelines;

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TextEncoderPool {
    private int poolSize;
    private ZooModel<String, NDList> model;
    private ArrayList<Predictor<String, NDList>> predictorList = new ArrayList<>();


    public TextEncoderPool(ZooModel<String, NDList> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<String, NDList> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<String, NDList> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<String, NDList> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<String, NDList> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<String, NDList> predictor : predictorList) {
            predictor.close();
        }

    }
}