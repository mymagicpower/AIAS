package top.aias.face.detection;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 人脸检测连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceDetPool {
    private int poolSize;
    private ZooModel<Image, DetectedObjects> model;
    private ArrayList<Predictor<Image, DetectedObjects>> predictorList = new ArrayList<>();


    public FaceDetPool(ZooModel<Image, DetectedObjects> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, DetectedObjects> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Image, DetectedObjects> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Image, DetectedObjects> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releaseDetector(Predictor<Image, DetectedObjects> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, DetectedObjects> detector : predictorList) {
            detector.close();
        }

    }
}