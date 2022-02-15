package me.aias.common.face;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 20, 2021
 */
public final class FaceFeatureModel {
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

    private Criteria<Image, float[]> detectCriteria(String layoutUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelName("face_feature") // specify model file prefix
                        .optModelUrls(layoutUri)
                        .optTranslator(new FaceFeatureTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
