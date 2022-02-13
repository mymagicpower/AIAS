package me.aias.service.impl;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.face.FaceDetectionModel;
import me.aias.common.face.FaceObject;
import me.aias.common.face.FaceUtil;
import me.aias.common.utils.DJLImageUtil;
import me.aias.service.DetectService;
import me.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 目标检测服务
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class DetectServiceImpl implements DetectService {
    @Value("${face.saveDetectedFace}")
    boolean save;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FaceDetectionModel faceDetectionModel;

    public List<FaceObject> faceDetect(BufferedImage image)
            throws IOException, ModelException, TranslateException {
        ZooModel<Image, DetectedObjects> model = faceDetectionModel.getModel();
        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            Image djlImg = ImageFactory.getInstance().fromImage(image);
            DetectedObjects detections = predictor.predict(djlImg);
            List<DetectedObjects.DetectedObject> list = detections.items();

            int index = 1;
            List<FaceObject> faceObjects = new ArrayList<>();

            for (DetectedObjects.DetectedObject detectedObject : list) {
                String className = detectedObject.getClassName();
                BoundingBox box = detectedObject.getBoundingBox();
                Rectangle rectangle = box.getBounds();

                // 抠人脸图
                Rectangle subImageRect =
                        FaceUtil.getSubImageRect(
                                image, rectangle, djlImg.getWidth(), djlImg.getHeight(), 0f);
                int x = (int) (subImageRect.getX());
                int y = (int) (subImageRect.getY());
                int w = (int) (subImageRect.getWidth());
                int h = (int) (subImageRect.getHeight());
                BufferedImage subImage = image.getSubimage(x, y, w, h);
                Image img = DJLImageUtil.bufferedImage2DJLImage(subImage);
                //获取特征向量
                List<Float> feature = featureService.faceFeature(img);

                FaceObject faceObject = new FaceObject();
                faceObject.setFeature(feature);
                faceObject.setBoundingBox(subImageRect);
                faceObjects.add(faceObject);
            }

            return faceObjects;
        }
    }
}
