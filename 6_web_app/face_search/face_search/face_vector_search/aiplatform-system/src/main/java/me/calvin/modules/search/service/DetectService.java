package me.calvin.modules.search.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.calvin.modules.search.domain.FaceObject;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;

import java.io.IOException;
import java.util.List;
/**
 * 人脸检测服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface DetectService {
    ImageInfoDto faceDetect(ImageInfo imageInfo) throws Exception;
    List<FaceObject> faceDetect(Image image) throws IOException, ModelException, TranslateException;
}
