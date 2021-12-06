package me.calvin.modules.search.service;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import me.calvin.modules.search.common.utils.face.FaceObject;
import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;

import java.io.IOException;
import java.util.List;

public interface DetectService {
    ImageInfoDto faceDetect(ImageInfo imageInfo) throws Exception;

    List<FaceObject> faceDetect(String facePath)
            throws IOException, ModelException, TranslateException;
}
