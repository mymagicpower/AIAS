package top.aias.img.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.img.model.*;
import top.aias.img.service.SegService;

/**
 * 图像分割服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class SegServiceImpl implements SegService {
    private Logger logger = LoggerFactory.getLogger(SegServiceImpl.class);

    @Autowired
    private BigUNetModel bigUNetModel;
    @Autowired
    private MidUNetModel midUNetModel;
    @Autowired
    private SmallUNetModel smallUNetModel;
    @Autowired
    private UNetHumanSegModel uNetHumanSegModel;
    @Autowired
    private IsNetModel isNetModel;
    @Autowired
    private UNetClothSegModel uNetClothSegModel;

    public Image generalSegBig(Image image) throws TranslateException {
        Image segImg = bigUNetModel.predict(image);
        return segImg;
    }

    public Image generalSegMid(Image image) throws TranslateException {
        Image segImg = midUNetModel.predict(image);
        return segImg;
    }

    public Image generalSegSmall(Image image) throws TranslateException {
        Image segImg = smallUNetModel.predict(image);
        return segImg;
    }

    public Image humanSeg(Image image) throws TranslateException {
        Image segImg = uNetHumanSegModel.predict(image);
        return segImg;
    }

    public Image animeSeg(Image image) throws TranslateException {
        Image segImg = isNetModel.predict(image);
        return segImg;
    }

    public Image clothSeg(Image image) throws TranslateException {
        Image segImg = uNetClothSegModel.predict(image);
        return segImg;
    }
}
