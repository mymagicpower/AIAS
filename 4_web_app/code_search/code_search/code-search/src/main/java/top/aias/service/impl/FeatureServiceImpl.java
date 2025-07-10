package top.aias.service.impl;

import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import top.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.model.vec.CodeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 特征提取服务
 * Feature service
 *
 * @author calvin
 * @mail 179209347@qq.com
 **/
@Slf4j
@Service
public class FeatureServiceImpl implements FeatureService {
    @Autowired
    private CodeModel codeModel;

    public List<Float> textFeature(String text) throws TranslateException {
            float[] embeddings = codeModel.predict(text);
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
