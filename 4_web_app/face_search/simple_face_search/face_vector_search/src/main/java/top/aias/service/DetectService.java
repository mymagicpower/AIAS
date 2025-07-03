package top.aias.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import top.aias.domain.FaceObject;

import java.io.IOException;
import java.util.List;

/**
 * 目标检测服务接口
 * Object detection service
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface DetectService {
    List<FaceObject> faceDetect(Image image)
            throws IOException, ModelException, TranslateException;
}
