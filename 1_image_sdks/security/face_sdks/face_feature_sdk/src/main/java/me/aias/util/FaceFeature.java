package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FaceFeature {

    public FaceFeature() {
    }

    public Criteria<Image, float[]> criteria() {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get("models/MobileFace.zip"))
                        .optModelName("inference")
                        .optTranslator(new FaceFeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    public float calculSimilar(float[] feature1, float[] feature2) {
        float ret = 0.0f;
        float mod1 = 0.0f;
        float mod2 = 0.0f;
        int length = feature1.length;
        for (int i = 0; i < length; ++i) {
            ret += feature1[i] * feature2[i];
            mod1 += feature1[i] * feature1[i];
            mod2 += feature2[i] * feature2[i];
        }
        return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2)));
    }
}
