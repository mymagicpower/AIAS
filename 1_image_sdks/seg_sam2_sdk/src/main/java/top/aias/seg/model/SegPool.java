package top.aias.seg.model;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ZooModel;
import top.aias.seg.translator.Sam2Translator;

import java.util.ArrayList;

/**
 * 图像分割连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SegPool {
    private int poolSize;
    private ZooModel<Sam2Translator.Sam2Input, DetectedObjects> model;
    private ArrayList<Predictor<Sam2Translator.Sam2Input, DetectedObjects>> predictorList = new ArrayList<>();


    public SegPool(ZooModel<Sam2Translator.Sam2Input, DetectedObjects> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Sam2Translator.Sam2Input, DetectedObjects> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Sam2Translator.Sam2Input, DetectedObjects> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Sam2Translator.Sam2Input, DetectedObjects> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Sam2Translator.Sam2Input, DetectedObjects> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Sam2Translator.Sam2Input, DetectedObjects> predictor : predictorList) {
            predictor.close();
        }

    }
}