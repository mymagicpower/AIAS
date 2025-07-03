package me.calvin.modules.search.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

import java.util.List;
/**
 * 人脸特征提取服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface FeatureService {
  List<Float> faceFeature(Image img) throws TranslateException;
}
