package me.aias.service;

import ai.djl.modality.cv.Image;
import me.aias.domain.DataBean;

import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
public interface OcrService {
    List<DataBean> getGeneralInfo(Image image);
}
