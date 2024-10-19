package top.aias.service.impl;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import top.aias.model.ImageEncoderModel;
import top.aias.model.TextEncoderModel;
import top.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public float[] textFeature(String text) throws TranslateException {
        ZooModel<String, float[]> model = textEncoderModel.getModel();
        try (Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] feature = predictor.predict(text);
            return feature;
        }
    }

    public float[] imageFeature(Image image) throws TranslateException {
        ZooModel<Image, float[]> model = imageEncoderModel.getModel();
        try (Predictor<Image, float[]> predictor = model.newPredictor()) {
            float[] feature = predictor.predict(image);
            return feature;
        }
    }
}
