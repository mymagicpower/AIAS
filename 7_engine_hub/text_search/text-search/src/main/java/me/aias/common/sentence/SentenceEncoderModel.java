package me.aias.common.sentence;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 句向量提取是指将语句映射至固定维度的实数向量。
 * 将不定长的句子用定长的向量表示，为NLP下游任务提供服务。
 * 支持 15 种语言：
 * Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.
 *
 * @author Calvin
 * @date 2021-12-19
 */
public final class SentenceEncoderModel {
    private ZooModel<String, float[]> model;

    public void init(String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri));
    }

    public ZooModel<String, float[]> getModel() {
        return model;
    }

    public void close() {
        this.model.close();
    }

    private Criteria<String, float[]> detectCriteria(String modelUri) {
        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new SentenceTransTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
//                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
