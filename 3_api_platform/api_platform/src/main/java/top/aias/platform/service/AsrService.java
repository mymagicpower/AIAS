package top.aias.platform.service;

import ai.djl.modality.audio.Audio;
import ai.djl.translate.TranslateException;

/**
 * 语音识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface AsrService {
    String enSpeechToText(Audio audio) throws TranslateException;

    String zhSpeechToText(Audio audio) throws TranslateException;
}
