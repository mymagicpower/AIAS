package me.aias.service.impl;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.aias.service.FeatureService;
import me.aias.common.voice.VoiceprintModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 特征提取服务
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private VoiceprintModel featureModel;

    public List<Float> feature(float[][] mag) throws TranslateException {
        ZooModel<float[][], float[]> model = featureModel.getModel();
        try (Predictor<float[][], float[]> predictor = model.newPredictor()) {
            float[] embeddings = predictor.predict(mag);
            List<Float> feature = new ArrayList<>();

            if (embeddings != null) {
                for (int i = 0; i < embeddings.length; i++) {
                    feature.add(embeddings[i]);
                }
            } else {
                return null;
            }
            return feature;
        }
    }
}
