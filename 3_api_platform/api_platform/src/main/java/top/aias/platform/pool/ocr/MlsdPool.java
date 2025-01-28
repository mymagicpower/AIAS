package top.aias.platform.pool.ocr;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;

import java.util.ArrayList;
/**
 * 文本转正连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class MlsdPool {
    private int poolSize;
    private ZooModel<Image, Image> model;
    private ArrayList<Predictor<Image, Image>> predictorList = new ArrayList<>();


    public MlsdPool(int poolSize, ZooModel<Image, Image> model) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Image, Image> predictor = model.newPredictor();
            predictorList.add(predictor);
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