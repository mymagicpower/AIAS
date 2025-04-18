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
public class VaeEncoderPool {
    private int poolSize;
    private ZooModel<Image, NDArray> model;
    private ArrayList<Predictor<Image, NDArray>> predictorList = new ArrayList<>();


    public VaeEncoderPool(ZooModel<Image, NDArray> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, NDArray> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Image, NDArray> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, NDArray> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Image, NDArray> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, NDArray> predictor : predictorList) {
            predictor.close();
        }

    }
}