package top.aias.seg.service;

import ai.djl.translate.TranslateException;
import top.aias.seg.bean.LabelBean;
import top.aias.seg.bean.ImageBean;

import java.io.IOException;
import java.util.List;

/**
 * 识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface ImageService {
    List<ImageBean> getDataList();

    ImageBean getImageInfo(String uid) throws IOException;

    void addImageInfo(ImageBean templateBean) throws IOException;

    String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException;
}
