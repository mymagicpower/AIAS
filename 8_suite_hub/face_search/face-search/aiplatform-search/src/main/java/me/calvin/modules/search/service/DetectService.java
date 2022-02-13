package me.calvin.modules.search.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.calvin.modules.search.common.face.FaceObject;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;

import java.util.List;

public interface DetectService {
    ImageInfoDto faceDetect(ImageInfo imageInfo) throws Exception;
    List<FaceObject> faceDetect(String name, Image djlImg) throws TranslateException;
}
