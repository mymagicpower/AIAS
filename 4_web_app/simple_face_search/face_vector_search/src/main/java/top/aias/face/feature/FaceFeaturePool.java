package top.aias.face.feature;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 人脸检测连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceFeaturePool {
    private int poolSize;
    private ZooModel<Image, float[]> model;
    private ArrayList<Predictor<Image, float[]>> predictorList = new ArrayList<>();

    public FaceFeaturePool( ZooModel<Image, float[]> model, int poolSize) {
        this.model = model;
        this.poolSize = poolSize;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, float[]> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Image, float[]> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, float[]> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releaseDetector(Predictor<Image, float[]> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, float[]> predictor : predictorList) {
            predictor.close();
        }

    }
}