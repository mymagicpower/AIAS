package me.aias.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.domain.DataBean;

import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
public interface FaceService {
    List<DataBean> detect(Image image);

    float[] feature(Image image);

    String compare(Image img1, Image img2) throws TranslateException;
}
