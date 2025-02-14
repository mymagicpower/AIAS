package top.aias.platform.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.seg.FaceSegModel;
import top.aias.platform.model.sr.SrModel;
import top.aias.platform.service.ImgSrService;
import top.aias.platform.utils.ImageUtils;

/**
 * 图像分割服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class ImgSrServiceImpl implements ImgSrService {
    private Logger logger = LoggerFactory.getLogger(ImgSrServiceImpl.class);

    @Autowired
    private FaceDetModel faceDetModel;
    @Autowired
    private FaceSegModel faceSegModel;
    @Autowired
    private FaceGanModel faceGanModel;
    @Autowired
    private SrModel srModel;

    public DetectedObjects faceDet(Image image) throws TranslateException {
        DetectedObjects detectedObjects = faceDetModel.predict(image);
        return detectedObjects;
    }

    public Image faceGan(Image image) throws TranslateException {
        Image img = faceGanModel.predict(image);
        return img;
    }

    public Image faceSeg(Image image) throws TranslateException {
        Image img = faceSegModel.predict(image);
        return img;
    }

    /**
     * 图像高清
     * 如果宽高都大于512，先缩小，再高清放大恢复原尺寸
     * 否则尺寸不变
     *
     * @param image
     * @return
     * @throws TranslateException
     */
    public Image imageHd(Image image) throws TranslateException {
        if(image.getHeight()> 512 && image.getHeight() > 512){
            image = image.resize(image.getWidth() / 4, image.getHeight() / 4, true);
            Image img = srModel.predict(image);
            return img;
        }else {
            Image img = srModel.predict(image);
            img = img.resize(img.getWidth() / 4, img.getHeight() / 4, true);
            return img;
        }
    }

    /**
     * 图像高清放大
     * 如果宽或高大于 1080，先缩小，再高清放大
     * 否则，直接放大4倍
     * 可以调高，最大支持多少，自己试验【取决于显存，内存，算法本身】。
     *
     * @param image
     * @return
     * @throws TranslateException
     */
    public Image imageSr(Image image) throws TranslateException {
        if(image.getHeight()> 1080 || image.getHeight() > 1080){
            image = image.resize(image.getWidth() / 4, image.getHeight() / 4, true);
            Image img = srModel.predict(image);
            return img;
        }else {
            Image img = srModel.predict(image);
            return img;
        }
    }
}
