package me.calvin.modules.search.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.config.FileProperties;
import me.calvin.modules.search.common.utils.FaceUtils;
import me.calvin.modules.search.domain.FaceObject;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.SimpleFaceObject;
import me.calvin.modules.search.face.FaceDetectionModel;
import me.calvin.modules.search.service.DetectService;
import me.calvin.modules.search.service.FeatureService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * 人脸检测服务
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
    private FaceDetectionModel faceDetectionModel;

    public ImageInfoDto faceDetect(ImageInfo imageInfo) throws Exception {
        // 人脸检测
        Path path = Paths.get(imageInfo.getFullPath());
        Image img = OpenCVImageFactory.getInstance().fromFile(path);
        List<FaceObject> faceObjects = this.faceDetect(img);

        List<SimpleFaceObject> faceList = new ArrayList<>();
        //转换检测对象，方便后面json转换
        for (FaceObject faceObject : faceObjects) {
            Rectangle rect = faceObject.getBoundingBox().getBounds();
            SimpleFaceObject faceDTO = new SimpleFaceObject();
            faceDTO.setScore(faceObject.getScore());
            faceDTO.setFeature(faceObject.getFeature());
            faceDTO.setX((int) rect.getX());
            faceDTO.setY((int) rect.getY());
            faceDTO.setWidth((int) rect.getWidth());
            faceDTO.setHeight((int) rect.getHeight());
            faceList.add(faceDTO);
        }

        // Bean转换
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        BeanUtils.copyProperties(imageInfo, imageInfoDto);
        imageInfoDto.setFaceObjects(faceList);

        return imageInfoDto;
    }

    public List<FaceObject> faceDetect(Image image)
            throws IOException, ModelException, TranslateException {
        DetectedObjects detections = faceDetectionModel.predict(image);
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
