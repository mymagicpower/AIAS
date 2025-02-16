package top.aias.platform.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

import java.io.IOException;

/**
 * 图像预处理服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface ImgPreProcessService {
    Image canny(Image image);
    Image contentShuffle(Image image);
    Image depthEstimationDpt(Image image) throws ModelException, TranslateException, IOException;
    Image depthEstimationMidas(Image image) throws ModelException, TranslateException, IOException;
    Image lineartAnime(Image image) throws ModelException, TranslateException, IOException;
    Image lineartCoarse(Image image) throws ModelException, TranslateException, IOException;
    Image lineart(Image image) throws ModelException, TranslateException, IOException;
    Image mlsd(Image image) throws ModelException, TranslateException, IOException;
    Image normalBae(Image image) throws ModelException, TranslateException, IOException;
    Image pose(Image image) throws TranslateException;
    Image scribbleHed(Image image) throws ModelException, TranslateException, IOException;
    Image scribblePidinet(Image image) throws ModelException, TranslateException, IOException;
    Image segUpernet(Image image) throws TranslateException;
    Image softedgeHed(Image image) throws ModelException, TranslateException, IOException;
    Image softedgePidinet(Image image) throws ModelException, TranslateException, IOException;
}

