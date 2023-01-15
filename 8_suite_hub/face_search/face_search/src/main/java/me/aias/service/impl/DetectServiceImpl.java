package me.aias.service.impl;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.face.FaceDetectionModel;
import me.aias.common.face.FaceObject;
import me.aias.common.face.FaceUtil;
import me.aias.common.utils.common.DJLImageUtil;
import me.aias.common.utils.common.NDArrayUtil;
import me.aias.common.utils.common.SVDUtil;
import me.aias.common.utils.opencv.FaceAlignment;
import me.aias.common.utils.opencv.OpenCVImageUtil;
import me.aias.config.FileProperties;
import me.aias.service.DetectService;
import me.aias.service.FeatureService;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.Mat;
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
    private final FileProperties properties;

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

    public List<FaceObject> faceDetect(String name, Image djlImg) throws TranslateException, ModelException, IOException {
        ZooModel<Image, DetectedObjects> model = faceDetectionModel.getModel();
        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            DetectedObjects detections = predictor.predict(djlImg);
            List<DetectedObjects.DetectedObject> list = detections.items();
            BufferedImage image = (BufferedImage) djlImg.getWrappedImage();
            int index = 0;
            List<FaceObject> faceObjects = new ArrayList<>();

            for (DetectedObjects.DetectedObject detectedObject : list) {
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

                // debug使用：保存，抠出的人脸图
                if (save) {
                    String savePath = properties.getPath().getPath() + "detected/";
                    if (!new File(savePath).exists()) {
                        new File(savePath).mkdirs();
                    }
                    DJLImageUtil.saveImage(subImage, name + "_" + index + ".png", savePath);
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

                    // debug使用：保存，对齐后的人脸图
                    if (save) {
                        String savePath = properties.getPath().getPath() + "aligned/";
                        if (!new File(savePath).exists()) {
                            new File(savePath).mkdirs();
                        }
                        DJLImageUtil.saveImage(
                                mat2BufferedImage, name + "_" + index + ".png", savePath);
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
}
