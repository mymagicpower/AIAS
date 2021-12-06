package me.calvin.modules.search.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calvin.config.FileProperties;
import me.calvin.modules.search.common.utils.common.DJLImageUtil;
import me.calvin.modules.search.common.utils.common.NDArrayUtil;
import me.calvin.modules.search.common.utils.common.SVDUtil;
import me.calvin.modules.search.common.utils.face.FaceDetectionModel;
import me.calvin.modules.search.common.utils.face.FaceObject;
import me.calvin.modules.search.common.utils.face.FaceUtil;
import me.calvin.modules.search.common.utils.opencv.FaceAlignment;
import me.calvin.modules.search.common.utils.opencv.OpenCVImageUtil;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.domain.SimpleFaceObject;
import me.calvin.modules.search.service.DetectService;
import me.calvin.modules.search.service.FeatureService;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        List<FaceObject> faceObjects = null;
        // 人脸检测
        faceObjects = this.faceDetect(imageInfo.getFullPath());

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

    public List<FaceObject> faceDetect(String facePath)
            throws IOException, ModelException, TranslateException {
        Path path = Paths.get(facePath);
        File file = path.toFile();
        BufferedImage image = ImageIO.read(file);
        Image djlImg = ImageFactory.getInstance().fromImage(image);
        DetectedObjects detections = faceDetectionModel.predict(djlImg);

        List<DetectedObjects.DetectedObject> list = detections.items();

        int index = 0;
        List<FaceObject> faceObjects = new ArrayList<>();

        for (DetectedObjects.DetectedObject detectedObject : list) {
            String className = detectedObject.getClassName();
            BoundingBox box = detectedObject.getBoundingBox();
            Rectangle rectangle = box.getBounds();

            // 抠人脸图
            // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
            Rectangle subImageRect =
                    FaceUtil.getSubImageRect(
                            image, rectangle, djlImg.getWidth(), djlImg.getHeight(), 1.0f);
            int x = (int) (subImageRect.getX());
            int y = (int) (subImageRect.getY());
            int w = (int) (subImageRect.getWidth());
            int h = (int) (subImageRect.getHeight());
            BufferedImage subImage = image.getSubimage(x, y, w, h);

            if (save) {
                // 保存，抠出的人脸图
                DJLImageUtil.saveImage(subImage, file.getName() + "_" + index + ".png", properties.getPath().getPath() + "detected/");
            }

            // 计算人脸关键点在子图中的新坐标
            List<Point> points = (List<Point>) box.getPath();
            double[][] pointsArray = FaceUtil.pointsArray(subImageRect, points);

            // 转 buffered image 图片格式
            BufferedImage converted3BGRsImg =
                    new BufferedImage(
                            subImage.getWidth(), subImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            converted3BGRsImg.getGraphics().drawImage(subImage, 0, 0, null);

            Mat mat = Java2DFrameUtils.toMat(converted3BGRsImg);

            try (NDManager manager = NDManager.newBaseManager()) {
                NDArray srcPoints = manager.create(pointsArray);
                NDArray dstPoints = SVDUtil.point112x112(manager);

                // 定制的5点仿射变换
                Mat svdMat = NDArrayUtil.toOpenCVMat(manager, srcPoints, dstPoints);
                // 换仿射变换矩阵
                mat = FaceAlignment.get5WarpAffineImg(mat, svdMat);

                // mat转bufferedImage类型
                BufferedImage mat2BufferedImage = OpenCVImageUtil.mat2BufferedImage(mat);
                int width = mat2BufferedImage.getWidth() > 112 ? 112 : mat2BufferedImage.getWidth();
                int height = mat2BufferedImage.getHeight() > 112 ? 112 : mat2BufferedImage.getHeight();
                mat2BufferedImage = mat2BufferedImage.getSubimage(0, 0, width, height);

                if (save) {
                    // 保存，对齐后的人脸图
                    DJLImageUtil.saveImage(
                            mat2BufferedImage, file.getName() + "_align_" + index + ".png", properties.getPath().getPath() + "detected/");
                }

                Image img = DJLImageUtil.bufferedImage2DJLImage(mat2BufferedImage);
                //获取特征向量
                List<Float> feature = featureService.faceFeature(img);

                FaceObject faceObject = new FaceObject();
                faceObject.setFeature(feature);
                faceObject.setBoundingBox(subImageRect);
                faceObjects.add(faceObject);
            }
        }

        return faceObjects;
    }
}
