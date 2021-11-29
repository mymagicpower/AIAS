package me.aias.ocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.ocr.model.DataBean;

import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public interface TableInferService {
    String getTableHtml(Image image) throws TranslateException;
    List<String> getTableHtmlList(Image image) throws TranslateException;
}
