package me.aias.service.impl;

import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.aias.service.FeatureService;
import me.aias.voice.VoiceprintModel;
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
        float[] embeddings = null;
        embeddings = featureModel.predict(mag);
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
