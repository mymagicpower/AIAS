package top.aias.service.impl;

import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.aias.domain.TextInfo;
import top.aias.model.NllbModel;
import top.aias.service.TextService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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