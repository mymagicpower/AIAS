package top.aias.search.utils;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 文本编码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TextEncoderPool {
    private int poolSize;
    private ZooModel<String, float[]> model;
    private ArrayList<Predictor<String, float[]>> predictorList = new ArrayList<>();


    public TextEncoderPool(ZooModel<String, float[]> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<String, float[]> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<String, float[]> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<String, float[]> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<String, float[]> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<String, float[]> predictor : predictorList) {
            predictor.close();
        }

    }
}