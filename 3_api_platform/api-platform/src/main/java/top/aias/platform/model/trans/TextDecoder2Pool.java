package top.aias.platform.model.trans;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;
import top.aias.platform.generate.LMOutput;

import java.util.ArrayList;

/**
 * 文本解码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TextDecoder2Pool {
    private int poolSize;
    private ZooModel<NDList, NDList> model;
    private ArrayList<Predictor<NDList, LMOutput>> predictorList = new ArrayList<>();


    public TextDecoder2Pool(ZooModel<NDList, NDList> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<NDList, LMOutput> predictor = model.newPredictor(new Decoder2Translator());
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<NDList, LMOutput> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<NDList, LMOutput> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<NDList, LMOutput> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<NDList, LMOutput> predictor : predictorList) {
            predictor.close();
        }

    }
}