package top.aias.service;

import ai.djl.translate.TranslateException;
import top.aias.domain.TextInfo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本翻译
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface TextService {
    /**
     * 文本翻译
     */
    String translate(String input, long srcLangId, long targetLangId) throws TranslateException;
}