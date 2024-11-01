package top.aias.ocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import top.aias.ocr.bean.RotatedBox;

import java.util.List;

/**
 * 文字识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface InferService {
    List<RotatedBox> getGeneralInfo(NDManager manager, Image image) throws TranslateException;
    Image getWarpImg(Image image) throws TranslateException;
}
