package top.aias.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import top.aias.face.feature.FaceFeatureModel;
import top.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 特征提取服务
 * Feature Service
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private FaceFeatureModel faceFeatureModel;

    public List<Float> faceFeature(Image img) throws TranslateException {
        float[] embeddings = faceFeatureModel.predict(img);
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
