package me.aias.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图片特征提取模型
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public final class ImageEncoderModel {
    private ZooModel<Image, float[]> model;

    public void init(String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri));
    }

    public ZooModel<Image, float[]> getModel() {
        return model;
    }

    public void close() {
        this.model.close();
    }

    private Criteria<Image, float[]> detectCriteria(String modelUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new ImageTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
//                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
