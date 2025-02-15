package top.aias.platform.service;

import ai.djl.ModelException;
import ai.djl.modality.audio.Audio;
import ai.djl.translate.TranslateException;
import ai.onnxruntime.OrtException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 语音识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface AsrService {
    String speechToText(Audio audio, Boolean isChinese) throws TranslateException, ModelException, IOException;

    String longSpeechToText(Path audioFilePath, Boolean isChinese) throws TranslateException, IOException, OrtException, ModelException;
}
