package top.aias.iocr.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import top.aias.iocr.bean.LabelBean;
import top.aias.iocr.bean.Point;
import top.aias.iocr.bean.RotatedBox;
import top.aias.iocr.bean.TemplateBean;
import top.aias.iocr.configuration.FileProperties;
import top.aias.iocr.model.RecognitionModel;
import top.aias.iocr.service.TemplateService;
import top.aias.iocr.utils.FileUtils;
import top.aias.iocr.utils.PerspectiveTransform;
import top.aias.iocr.utils.PointUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板识别服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
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

    @Value("${image.debug}")
    private boolean debug;

    @Value("${distance.type}")
    private String distanceType;

    @Value("${image.maxNum}")
    private int maxNum;
    @Value("${image.disThreshold}")
    private double disThreshold;

    public Map<String, String> getImageInfo(TemplateBean templateBean, Image image) throws TranslateException, IOException {
        List<LabelBean> anchorlabels = getLabelDataByType(templateBean.getLabelData(), "anchor");
        List<LabelBean> contentLabels = getLabelDataByType(templateBean.getLabelData(), "rectangle");

        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        Path imageFile = Paths.get(fileRelativePath + "images/" + templateBean.getImageName());
//        BufferedImage templateImg = ImageIO.read(new File(fileRelativePath + "images/" + templateBean.getImageName()));
        Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);
        try (NDManager manager = NDManager.newBaseManager()) {
            Map<String, String> hashMap = PerspectiveTransform.recognize(manager, templateImg, recognitionModel, image, anchorlabels, contentLabels, fileRelativePath, distanceType, maxNum, disThreshold, debug);
            return hashMap;
        }
    }

    /**
     * 将参考框的坐标替换为对应自动检测框的坐标
     * @param templateBean
     * @param templateTextsDet
     * @return
     */
    public List<LabelBean> getImageInfo(TemplateBean templateBean, List<RotatedBox> templateTextsDet) {
        List<LabelBean> labels = templateBean.getLabelData();

        // 模版文本检测 1
        // Text detection area
        for (RotatedBox rotatedBox : templateTextsDet) {
            List<Point> points = new ArrayList<>();
            float[] pointsArr = rotatedBox.getBox().toFloatArray();
            for (int i = 0; i < 4; i++) {
                Point point = new Point((int) pointsArr[2 * i], (int) pointsArr[2 * i + 1]);
                points.add(point);
            }

            String detectedText = rotatedBox.getText();

            // 替换对应检测框的坐标
            for (int j = 0; j < labels.size(); j++) {
                String labelText = labels.get(j).getValue();
                if (detectedText.equals(labelText)) {
                    labels.get(j).setPoints(points);
                }
            }

        }


        return labels;
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
     * 获取模板信息
     * Get Template
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
     * 获取模板图片检测信息
     * Get Template Recognition Info
     *
     * @param uid
     */
    public TemplateBean getTemplateRecInfo(String uid) throws IOException {
        TemplateBean template = null;
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/") + "templates/recinfo/";
        String json = FileUtils.readFile(fileRelativePath, uid + ".json");
        if (!StringUtils.isBlank(json)) {
            template = new Gson().fromJson(json, new TypeToken<TemplateBean>() {
            }.getType());
        }
        return template;
    }

    /**
     * 新增模板
     * Add Template
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
        // Save template list data
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 保存模版数据
        // Save template data
        json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/", templateBean.getUid() + ".json", json);
    }

    /**
     * 更新模板
     * Update Template
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
        // Save template list data
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 保存模版数据
        // Save template data
        json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/", templateBean.getUid() + ".json", json);
    }

    /**
     * 更新模板自动检测信息
     * Update Template
     *
     * @param templateBean
     */
    public synchronized void updateTemplateRecInfo(TemplateBean templateBean) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileProperties.ElPath path = properties.getPath();
        // 保存模版列表数据
        // Save template list data
        String fileRelativePath = path.getPath().replace("\\", "/");
        // 保存数据
        // Save data
        String json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/recinfo/", templateBean.getUid() + ".json", json);
    }

    /**
     * 删除模板
     * Delete Template
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
        // Save template data
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 删除模版数据
        // Delete template data
        FileUtils.removeFile(fileRelativePath + "templates/", uid + ".json");
    }


    public String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException {
        TemplateBean template = getTemplate(uid);
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        Path imageFile = Paths.get(fileRelativePath + "images/" + template.getImageName());
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
//        int x = labelData.getPoints().get(0).getX();
//        int y = labelData.getPoints().get(0).getY();
//        int w = labelData.getPoints().get(1).getX() - labelData.getPoints().get(0).getX();
//        int h = labelData.getPoints().get(3).getY() - labelData.getPoints().get(0).getY();
        int[] rect = PointUtils.rectXYWH(labelData.getPoints());
        Image subImage = image.getSubImage(rect[0], rect[1], rect[2], rect[3]);
        String result = recognitionModel.predictSingleLineText(subImage);
        return result;
    }

    public List<LabelBean> getLabelDataByType(List<LabelBean> labelData, String type) {
        List<LabelBean> labels = new ArrayList<>();
        for (int i = 0; i < labelData.size(); i++) {
            if (labelData.get(i).getType().equals(type)) {
                labels.add(labelData.get(i));
            }
        }
        return labels;
    }
}
