package top.aias.platform.pool.asr;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;
import top.aias.platform.translator.asr.AsrEncoderTranslator;

import java.util.ArrayList;

/**
 * 编码连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class EncoderPool {
    private int poolSize;
    private ZooModel<NDList, NDList> model;
    private ArrayList<Predictor<Audio, NDList>> predictorList = new ArrayList<>();

    public EncoderPool(int poolSize, ZooModel<NDList, NDList> model, int kvLength, int encoderIndex) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Audio, NDList> predictor = model.newPredictor(new AsrEncoderTranslator(kvLength, encoderIndex));
            predictorList.add(predictor);
        }
    }

    public synchronized Predictor<Audio, NDList> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Audio, NDList> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Audio, NDList> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Audio, NDList> predictor : predictorList) {
            predictor.close();
        }

    }
}