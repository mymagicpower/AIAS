package top.aias.platform.service.impl;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Mask;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.platform.bean.ImageBean;
import top.aias.platform.bean.LabelBean;
import top.aias.platform.bean.Sam2Input;
import top.aias.platform.configuration.FileProperties;
import top.aias.platform.model.seg.*;
import top.aias.platform.service.SegService;
import top.aias.platform.utils.FileUtils;
import top.aias.platform.utils.ImageUtils;
import top.aias.platform.utils.OpenCVUtils;
import top.aias.platform.utils.PointUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

