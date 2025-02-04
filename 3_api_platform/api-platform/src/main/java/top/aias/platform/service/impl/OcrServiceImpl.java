package top.aias.platform.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import top.aias.platform.bean.RotatedBox;
import top.aias.platform.model.mlsd.MlsdSquareModel;
import top.aias.platform.model.ocr.RecognitionModel;
import top.aias.platform.service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文字识别服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class OcrServiceImpl implements OcrService {
    private Logger logger = LoggerFactory.getLogger(OcrServiceImpl.class);

    @Autowired
    private RecognitionModel recognitionModel;

    @Autowired
    private MlsdSquareModel mlsdSquareModel;

    public List<RotatedBox> getGeneralInfo(NDManager manager, Image image) throws TranslateException {
        List<RotatedBox> detectedObjects = recognitionModel.predict(manager, image);
        return detectedObjects;
    }

    public Image getWarpImg(Image image) throws TranslateException {
        Image cropImg = mlsdSquareModel.predict(image);
        return cropImg;
    }
}
