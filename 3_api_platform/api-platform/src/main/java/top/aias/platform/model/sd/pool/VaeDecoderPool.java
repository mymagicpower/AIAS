package top.aias.platform.model.sd.pool;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
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
public class VaeDecoderPool {
    private int poolSize;
    private ZooModel<NDArray, Image> model;
    private ArrayList<Predictor<NDArray, Image>> predictorList = new ArrayList<>();


    public VaeDecoderPool(ZooModel<NDArray, Image> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<NDArray, Image> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<NDArray, Image> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<NDArray, Image> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<NDArray, Image> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<NDArray, Image> predictor : predictorList) {
            predictor.close();
        }

    }
}