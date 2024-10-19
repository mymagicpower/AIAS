package top.aias.service.impl;

import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.common.model.TextEncoderModel;
import top.aias.service.FeatureService;

import java.util.ArrayList;
import java.util.List;

/**
 * 特征提取服务
 * Feature service
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private TextEncoderModel textEncoderModel;

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
}
