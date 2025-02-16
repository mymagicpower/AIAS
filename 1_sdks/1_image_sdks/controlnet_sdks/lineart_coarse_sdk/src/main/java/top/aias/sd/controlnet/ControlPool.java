package top.aias.sd.controlnet;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class ControlPool {
    private int poolSize;
    private ZooModel<Image, Image> model;
    private ArrayList<Predictor<Image, Image>> predictorList = new ArrayList<>();


    public ControlPool(ZooModel<Image, Image> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, Image> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Image, Image> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, Image> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Image, Image> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, Image> predictor : predictorList) {
            predictor.close();
        }

    }
}