package top.aias.platform.service.impl;

import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.platform.model.trans.NllbModel;
import top.aias.platform.service.TextService;

/**
 * 文本翻译服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class TextServiceImpl implements TextService {
    private Logger logger = LoggerFactory.getLogger(TextServiceImpl.class);

    @Autowired
    private NllbModel nllbModel;

    public TextServiceImpl() {
    }

    /**
     * 文本翻译
     */
    public String translate(String input, long srcLangId, long targetLangId) throws TranslateException {
        String result = nllbModel.translate(input, srcLangId, targetLangId);
        return result;
    }
}