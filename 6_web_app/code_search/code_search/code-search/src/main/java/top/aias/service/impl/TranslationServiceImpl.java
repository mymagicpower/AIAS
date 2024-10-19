package top.aias.service.impl;

import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.model.trans.TranslationModel;
import top.aias.model.vec.CodeModel;
import top.aias.service.FeatureService;
import top.aias.service.TranslationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 翻译服务
 *
 * @author calvin
 * @mail 179209347@qq.com
 **/
@Slf4j
@Service
public class TranslationServiceImpl implements TranslationService {
    @Autowired
    private TranslationModel translationModel;

    public String translate(String input) throws TranslateException {
        String text = translationModel.translate(input);
        return text;
    }
}
