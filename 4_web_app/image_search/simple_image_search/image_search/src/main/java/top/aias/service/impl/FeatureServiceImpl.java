package top.aias.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.model.ImageEncoderModel;
import top.aias.service.FeatureService;

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
    private ImageEncoderModel imageEncoderModel;

    public float[] imageFeature(Image image) throws TranslateException {
        float[] feature = imageEncoderModel.predict(image);
        return feature;
    }
}
