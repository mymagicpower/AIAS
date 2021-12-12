package me.aias.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.face.FaceFeatureModel;
import me.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 特征提取服务
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private FaceFeatureModel faceFeatureModel;

    public List<Float> faceFeature(Image img) throws TranslateException {
        float[] embeddings = null;
        embeddings = faceFeatureModel.predict(img);
        List<Float> feature = new ArrayList<>();

        if (embeddings != null) {
            for (int i = 0; i < embeddings.length; i++) {
                feature.add(new Float(embeddings[i]));
            }
        } else {
            return null;
        }
        return feature;
    }
}
