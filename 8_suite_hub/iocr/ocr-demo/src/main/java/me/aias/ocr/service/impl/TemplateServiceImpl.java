package me.aias.ocr.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.aias.ocr.configuration.FileProperties;
import me.aias.ocr.inference.PerspectiveRecogition;
import me.aias.ocr.inference.RecognitionModel;
import me.aias.ocr.model.LabelBean;
import me.aias.ocr.model.TemplateBean;
import me.aias.ocr.service.TemplateService;
import me.aias.ocr.utils.FileUtils;
import me.aias.ocr.utils.PointUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Service
public class TemplateServiceImpl implements TemplateService {
    private static final String TEMPLATE_LIST_FILE = "templates.json";
    private Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    /**
     * file configuration
     */
    @Autowired
    private FileProperties properties;

    /**
     * ocr recognition model
     */
    @Autowired
    private RecognitionModel recognitionModel;

    @Value("${model.type}")
    private String type;

    @Value("${image.debug}")
    private boolean debug;

    @Value("${distance.type}")
    private String distance;

    public Map<String, String> getImageInfo(TemplateBean templateBean, Image image) throws TranslateException, IOException {
        List<LabelBean> anchorlabels = getLabelDataByType(templateBean.getLabelData(), "anchor");
        List<LabelBean> contentLabels = getLabelDataByType(templateBean.getLabelData(), "rectangle");

        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        BufferedImage templateImg = ImageIO.read(new File(fileRelativePath + "images/" + templateBean.getImageName()));

        try (NDManager manager = NDManager.newBaseManager()) {
            Map<String, String> hashMap = PerspectiveRecogition.recognize(manager, templateImg, recognitionModel, image, anchorlabels, contentLabels, fileRelativePath, distance, debug);
            return hashMap;
        }
    }

    public List<TemplateBean> getTemplateList() {
        List<TemplateBean> templateList = null;
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        String json = null;
        try {
            json = FileUtils.readFile(fileRelativePath, TEMPLATE_LIST_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(json)) {
            templateList = new Gson().fromJson(json, new TypeToken<List<TemplateBean>>() {
            }.getType());
        } else {
            templateList = new ArrayList<>();
        }
        return templateList;
    }

    /**
     * 获取模板
     *
     * @param uid
     */
    public TemplateBean getTemplate(String uid) throws IOException {
        TemplateBean template = null;
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/") + "templates/";
        String json = FileUtils.readFile(fileRelativePath, uid + ".json");
        if (!StringUtils.isBlank(json)) {
            template = new Gson().fromJson(json, new TypeToken<TemplateBean>() {
            }.getType());
        }
        return template;
    }

    /**
     * 新增模板
     *
     * @param templateBean
     */
    public synchronized void addTemplate(TemplateBean templateBean) throws IOException {
        List<TemplateBean> templateList = getTemplateList();
        templateBean.setLabelData(null);
        templateList.add(templateBean);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(templateList);
        FileProperties.ElPath path = properties.getPath();
        // 保存模版列表数据
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 保存模版数据
        json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/", templateBean.getUid() + ".json", json);
    }

    /**
     * 更新模板
     *
     * @param templateBean
     */
    public synchronized void updateTemplate(TemplateBean templateBean) throws IOException {
        List<TemplateBean> templateList = getTemplateList();
        for (TemplateBean item : templateList) {
            if (item.getUid().equals(templateBean.getUid())) {
                BeanUtils.copyProperties(templateBean, item);
                item.setLabelData(null);
                break;
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(templateList);
        FileProperties.ElPath path = properties.getPath();
        // 保存模版列表数据
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 保存模版数据
        json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/", templateBean.getUid() + ".json", json);
    }

    /**
     * 删除模板
     *
     * @param uid
     */
    public synchronized void removeTemplate(String uid) throws IOException {
        List<TemplateBean> templateList = getTemplateList();
        for (int i = 0; i < templateList.size(); i++) {
            if (templateList.get(i).getUid().equals(uid)) {
                templateList.remove(i);
                break;
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(templateList);
        FileProperties.ElPath path = properties.getPath();
        // 保存模版列表数据
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 删除模版数据
        FileUtils.removeFile(fileRelativePath + "templates/", uid + ".json");
    }


    public String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException {
        TemplateBean template = getTemplate(uid);
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        BufferedImage img = ImageIO.read(new File(fileRelativePath + "images/" + template.getImageName()));
        Image image = ImageFactory.getInstance().fromImage(img);
//        int x = labelData.getPoints().get(0).getX();
//        int y = labelData.getPoints().get(0).getY();
//        int w = labelData.getPoints().get(1).getX() - labelData.getPoints().get(0).getX();
//        int h = labelData.getPoints().get(3).getY() - labelData.getPoints().get(0).getY();
        int[] rect = PointUtils.rectXYWH(labelData.getPoints());
        Image subImage = image.getSubImage(rect[0], rect[1], rect[2], rect[3]);
        String result = recognitionModel.predictSingleLineText(subImage);
        return result;
    }

    private List<LabelBean> getLabelDataByType(List<LabelBean> labelData, String type) {
        List<LabelBean> labels = new ArrayList<>();
        for (int i = 0; i < labelData.size(); i++) {
            if (labelData.get(i).getType().equals(type)) {
                labels.add(labelData.get(i));
            }
        }
        return labels;
    }
}
