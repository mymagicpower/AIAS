package me.aias.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.List;

/**
 * 特征提取服务接口
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface FeatureService {
    List<Float> textFeature(String text) throws IOException, ModelException, TranslateException;
}
