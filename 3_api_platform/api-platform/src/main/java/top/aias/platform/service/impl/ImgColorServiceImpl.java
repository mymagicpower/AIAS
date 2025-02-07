package top.aias.platform.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.platform.model.color.DdcolorModel;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.seg.FaceSegModel;
import top.aias.platform.model.sr.SrModel;
import top.aias.platform.service.ImgColorService;
import top.aias.platform.service.ImgSrService;

/**
 * 黑白照片上色服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class ImgColorServiceImpl implements ImgColorService {
    private Logger logger = LoggerFactory.getLogger(ImgColorServiceImpl.class);

    @Autowired
    private DdcolorModel ddcolorModel;

    public Image colorize(Image image) throws TranslateException {
        Image img = ddcolorModel.predict(image);
        return img;
    }
}
