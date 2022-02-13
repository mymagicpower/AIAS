package me.aias.common.face;

import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
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
public final class FaceDetectionModel {
    private ZooModel<Image, DetectedObjects> model;

    public void init(String modelUri, float shrink, float threshold) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri, shrink, threshold));
    }

    public ZooModel<Image, DetectedObjects> getModel() {
        return model;
    }

    public void close() {
        this.model.close();
    }

    private Criteria<Image, DetectedObjects> detectCriteria(String modelUrl, float shrink, float threshold) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelUrls(modelUrl)
                        .optTranslator(new FaceDetectionTranslator(shrink, threshold))
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }
}
