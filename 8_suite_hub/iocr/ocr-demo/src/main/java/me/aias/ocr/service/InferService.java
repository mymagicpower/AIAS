package me.aias.ocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.ocr.model.DataBean;
import me.aias.ocr.model.TemplateBean;

import java.util.List;
import java.util.Map;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public interface InferService {
    List<DataBean> getGeneralInfo(Image image) throws TranslateException;
}
