package top.aias.sd.preprocess.pose;

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceHandPool {
    private int poolSize;
    private ZooModel<NDArray, NDArray> model;
    private ArrayList<Predictor<NDArray, NDArray>> predictorList = new ArrayList<>();


    public FaceHandPool(ZooModel<NDArray, NDArray> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<NDArray, NDArray> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<NDArray, NDArray> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<NDArray, NDArray> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<NDArray, NDArray> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<NDArray, NDArray> predictor : predictorList) {
            predictor.close();
        }

    }
}