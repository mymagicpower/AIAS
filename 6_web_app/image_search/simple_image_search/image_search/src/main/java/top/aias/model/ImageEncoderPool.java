package top.aias.model;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;

/**
 * 图像编码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class ImageEncoderPool {
    private int poolSize;
    private ZooModel<Image, float[]> model;
    private ArrayList<Predictor<Image, float[]>> predictorList = new ArrayList<>();


    public ImageEncoderPool(ZooModel<Image, float[]> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

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

    public synchronized void releasePredictor(Predictor<Image, float[]> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Image, float[]> detector : predictorList) {
            detector.close();
        }

    }
}