package top.aias.platform.service;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;

/**
 * 黑白照片上色服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface ImgColorService {
    Image colorize(Image image) throws TranslateException;
}
