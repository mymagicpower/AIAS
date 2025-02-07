package top.aias.trans.model;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;
import top.aias.trans.generate.CausalLMOutput;

import java.util.ArrayList;

/**
 * 解码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 */
public class Decoder2Pool {
    private int poolSize;
    private ZooModel<NDList, NDList> model;
    private ArrayList<Predictor<NDList, CausalLMOutput>> predictorList = new ArrayList<>();


    public Decoder2Pool(ZooModel<NDList, NDList> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<NDList, CausalLMOutput> predictor = model.newPredictor(new Decoder2Translator());
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<NDList, CausalLMOutput> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<NDList, CausalLMOutput> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<NDList, CausalLMOutput> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<NDList, CausalLMOutput> predictor : predictorList) {
            predictor.close();
        }

    }
}