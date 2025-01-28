package top.aias.platform.pool.asr;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;
import top.aias.platform.bean.DecoderOutput;
import top.aias.platform.translator.asr.AsrDecoderTranslator;

import java.util.ArrayList;

/**
 * 解码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class DecoderPool {
    private int poolSize;
    private ZooModel<NDList, NDList> model;
    private ArrayList<Predictor<NDList, DecoderOutput>> predictorList = new ArrayList<>();

    public DecoderPool(int poolSize, ZooModel<NDList, NDList> model, int kvLength) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<NDList, DecoderOutput> predictor = model.newPredictor(new AsrDecoderTranslator(kvLength));
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<NDList, DecoderOutput> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<NDList, DecoderOutput> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<NDList, DecoderOutput> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<NDList, DecoderOutput> predictor : predictorList) {
            predictor.close();
        }

    }
}