package me.calvin.modules.search.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import me.calvin.modules.search.model.ImageEncoderModel;
import me.calvin.modules.search.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 特征提取服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private ImageEncoderModel imageEncoderModel;

    public List<Float> imageFeature(Image img) throws TranslateException {
        float[] embeddings = imageEncoderModel.predict(img);
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
