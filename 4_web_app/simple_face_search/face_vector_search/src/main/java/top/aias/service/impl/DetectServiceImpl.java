package top.aias.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.aias.common.utils.FaceUtils;
import top.aias.config.FileProperties;
import top.aias.domain.FaceObject;
import top.aias.face.detection.FaceDetectionModel;
import top.aias.service.DetectService;
import top.aias.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 目标检测服务
 * Object detection service
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class DetectServiceImpl implements DetectService {
    private final FileProperties properties;

    @Value("${face.saveDetectedFace}")
    boolean save;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FaceDetectionModel faceDetModel;

    public List<FaceObject> faceDetect(Image image)
            throws IOException, ModelException, TranslateException {
        DetectedObjects detections = faceDetModel.predict(image);
        List<DetectedObjects.DetectedObject> list = detections.items();

        List<FaceObject> faceObjects = new ArrayList<>();

        if (list.size() == 0)
            return faceObjects;

        for (int i = 0; i < list.size(); i++) {
            BoundingBox box = list.get(i).getBoundingBox();
            // 只有一张人脸的情况，人脸可以进一步对112 * 112 图片按比例裁剪，去除冗余信息，比如头发等，以提高精度
            Image cropImg = FaceUtils.align(image, box, save);

            //获取特征向量
            // get feature vector
            List<Float> feature = featureService.faceFeature(cropImg);

            FaceObject faceObject = new FaceObject();
            faceObject.setFeature(feature);
            Rectangle subImageRect = FaceUtils.getSubImageRect(box.getBounds(), image.getWidth(), image.getHeight(), 0f);
            faceObject.setBoundingBox(subImageRect);
            faceObjects.add(faceObject);
        }

        return faceObjects;
    }
}
