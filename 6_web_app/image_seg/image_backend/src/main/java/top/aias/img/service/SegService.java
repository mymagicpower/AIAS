package top.aias.img.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

/**
 * 图像分割服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface SegService {
    Image generalSegBig(Image image) throws TranslateException;
    Image generalSegMid(Image image) throws TranslateException;
    Image generalSegSmall(Image image) throws TranslateException;
    Image humanSeg(Image image) throws TranslateException;
    Image animeSeg(Image image) throws TranslateException;
    Image clothSeg(Image image) throws TranslateException;
}
