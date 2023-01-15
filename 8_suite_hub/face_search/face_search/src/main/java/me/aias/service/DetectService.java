package me.aias.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.common.face.FaceObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * 目标检测服务接口
 *
 * @author Calvin
 * @date 2021-12-12
 **/
public interface DetectService {
    List<FaceObject> faceDetect(BufferedImage image)
            throws IOException, ModelException, TranslateException;
    List<FaceObject> faceDetect(String name, Image djlImg) throws TranslateException, ModelException, IOException;
}
