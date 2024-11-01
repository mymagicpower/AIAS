package top.aias.ocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

import java.util.List;

/**
 * 表格识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface TableInferService {
    String getTableHtml(Image image) throws TranslateException;
    List<String> getTableHtmlList(Image image) throws TranslateException;
}
