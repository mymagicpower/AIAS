package top.aias.service;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.List;

/**
 * 翻译服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 **/
public interface TranslationService {
    String translate(String text) throws TranslateException;
}
