package me.aias.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文本特征提取模型
 * Text encoder model
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public final class TextEncoderModel {
    private ZooModel<String, float[]> model;

    public void init(String modelUri, boolean isChinese) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri, isChinese));
    }

    public void close() {
        this.model.close();
    }

    public ZooModel<String, float[]> getModel() {
        return model;
    }

    private Criteria<String, float[]> detectCriteria(String modelUri, boolean isChinese) {
        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new TextTranslator(isChinese))
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
//                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
