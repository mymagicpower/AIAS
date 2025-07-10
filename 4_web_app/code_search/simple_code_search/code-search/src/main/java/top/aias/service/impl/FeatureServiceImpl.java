package top.aias.service.impl;

import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.model.CodeModel;
import top.aias.model.code2vec.Code2VecModel;
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
    private CodeModel codeModel;

    public float[] textFeature(String text) throws TranslateException {
        float[] feature = codeModel.predict(text);
        return feature;
    }
}
