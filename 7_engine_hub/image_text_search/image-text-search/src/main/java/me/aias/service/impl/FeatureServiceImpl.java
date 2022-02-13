package me.aias.service.impl;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.aias.model.ImageEncoderModel;
import me.aias.model.TextEncoderModel;
import me.aias.service.FeatureService;
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
    private TextEncoderModel textEncoderModel;
    @Autowired
    private ImageEncoderModel imageEncoderModel;

    public List<Float> textFeature(String text) throws TranslateException {
        ZooModel<String, float[]> model = textEncoderModel.getModel();
        try (Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] embeddings = predictor.predict(text);
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

    public List<Float> imageFeature(Image image) throws TranslateException {
        ZooModel<Image, float[]> model = imageEncoderModel.getModel();
        try (Predictor<Image, float[]> predictor = model.newPredictor()) {
            float[] embeddings = predictor.predict(image);
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
