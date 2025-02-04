package top.aias.platform.service;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;

/**
 * 图像服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface ImgSrService {
    DetectedObjects faceDet(Image image) throws TranslateException;
    Image faceGan(Image image) throws TranslateException;
    Image faceSeg(Image image) throws TranslateException;
    Image imageSr(Image image) throws TranslateException;
}
