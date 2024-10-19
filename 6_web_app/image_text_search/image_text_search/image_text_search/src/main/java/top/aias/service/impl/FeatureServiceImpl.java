package top.aias.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.model.ImageEncoderModel;
import top.aias.model.TextEncoderModel;
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
    @Autowired
    private ImageEncoderModel imageEncoderModel;

    public List<Float> textFeature(String text) throws TranslateException {
        float[] embeddings = textEncoderModel.predict(text);
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

    public List<Float> imageFeature(Image image) throws TranslateException {
        float[] embeddings = imageEncoderModel.predict(image);
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
