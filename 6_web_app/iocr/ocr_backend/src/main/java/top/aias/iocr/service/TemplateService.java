package top.aias.iocr.service;

import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import top.aias.iocr.bean.LabelBean;
import top.aias.iocr.bean.RotatedBox;
import top.aias.iocr.bean.TemplateBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模板识别接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public interface TemplateService {
//    Map<String, String> getMlsdImageInfo(TemplateBean templateBean, Image image) throws TranslateException, IOException;

    List<LabelBean> getImageInfo(TemplateBean templateBean, List<RotatedBox> templateTextsDet);

    Map<String, String> getImageInfo(TemplateBean templateBean, Image image) throws TranslateException, IOException;
    
    List<TemplateBean> getTemplateList();

    TemplateBean getTemplate(String uid) throws IOException;

    void addTemplate(TemplateBean templateBean) throws IOException;

    void updateTemplate(TemplateBean templateBean) throws IOException;

    TemplateBean getTemplateRecInfo(String uid) throws IOException;

    void updateTemplateRecInfo(TemplateBean templateBean) throws IOException;

    void removeTemplate(String uid) throws IOException;

    String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException;

    List<LabelBean> getLabelDataByType(List<LabelBean> labelData, String type);
}
