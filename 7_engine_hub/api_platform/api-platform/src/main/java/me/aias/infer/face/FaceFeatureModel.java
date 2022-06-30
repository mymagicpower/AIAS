package me.aias.infer.face;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 20, 2021
 */
public final class FaceFeatureModel {

    private ZooModel<Image, float[]> model;
    private Predictor<Image, float[]> predictor;

    public void init(String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri));
        this.predictor = model.newPredictor();
    }

    public void close() {
        this.model.close();
        this.predictor.close();
    }

    public float[] predict(Image image) throws TranslateException {
        return predictor.predict(image);
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
