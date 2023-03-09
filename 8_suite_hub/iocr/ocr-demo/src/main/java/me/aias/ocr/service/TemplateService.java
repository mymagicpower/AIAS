package me.aias.ocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.ocr.model.LabelBean;
import me.aias.ocr.model.TemplateBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public interface TemplateService {
    Map<String, String> getImageInfo(TemplateBean templateBean, Image image) throws TranslateException, IOException;
    
    List<TemplateBean> getTemplateList();

    TemplateBean getTemplate(String uid) throws IOException;

    void addTemplate(TemplateBean templateBean) throws IOException;

    void updateTemplate(TemplateBean templateBean) throws IOException;

    void removeTemplate(String uid) throws IOException;

    String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException;

}
