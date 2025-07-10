package top.aias.iocr.controller;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.iocr.bean.*;
import top.aias.iocr.configuration.FileProperties;
import top.aias.iocr.service.InferService;
import top.aias.iocr.service.TemplateService;
import top.aias.iocr.utils.FileUtils;
import top.aias.iocr.utils.UUIDUtils;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 自定义模版文字识别
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "自定义模版文字识别 - Custom Template Text Recognition")
@RestController
@Configuration
@RequestMapping("/template")
public class TemplateController {
    private Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    private TemplateService ocrTemplateService;
    @Autowired
    private InferService inferService;

    /**
     * 文件配置
     * File Configuration
     */
    @Autowired
    private FileProperties properties;

    @ApiOperation(value = "获取模版信息 Get Template Information")
    @GetMapping(value = "/getTemplate", produces = "application/json;charset=utf-8")
    public ResultBean getTemplate(@RequestParam(value = "uid") String uid) {
        try {
            TemplateBean templateBean = ocrTemplateService.getTemplate(uid);
            return ResultBean.success().add("result", templateBean);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "获取模版列表 Get Template List")
    @GetMapping(value = "/getTemplates", produces = "application/json;charset=utf-8")
    public ResultBean getTemplatesList() {
        try {
            return ResultBean.success().add("result", ocrTemplateService.getTemplateList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "更新模板信息 Update Template Information")
    @PostMapping(value = "/updateTemplate", consumes = "application/json;charset=utf-8")
    public ResultBean updateTemplate(@RequestBody TemplateBean templateBean) {
        try (NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {

            // 检测锚点框的数量
            List<LabelBean> anchorlabels = ocrTemplateService.getLabelDataByType(templateBean.getLabelData(), "anchor");
            if(anchorlabels.size() < 4){
                return ResultBean.failure().add("message", "锚点参考框至少需要4个，不能小于4个。");
            }

            // 更新手工标注的模板信息
            ocrTemplateService.updateTemplate(templateBean);

            // 新建或者更新手工标注的模板信息
            FileProperties.ElPath path = properties.getPath();
            String imagePath = path.getPath().replace("\\", "/") + "images/" + templateBean.getImageName();
            Path imageFile = Paths.get(imagePath);
            Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);
            List<RotatedBox> detections = inferService.getGeneralInfo(manager, templateImg);
            // 将手工标注的参考框坐标替换为对应自动检测框的坐标，手工标注的会有几个像素的偏移，影响透视变换的效果
            List<LabelBean> updatedLabelData = ocrTemplateService.getImageInfo(templateBean, detections);

            String templatePath = path.getPath().replace("\\", "/") + "templates/recinfo/";
            FileUtils.checkAndCreatePath(templatePath);
            templateBean.setLabelData(updatedLabelData);
            ocrTemplateService.updateTemplateRecInfo(templateBean);
            return ResultBean.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "删除模板 Remove Template")
    @PostMapping(value = "/removeTemplate", produces = "application/json;charset=utf-8")
    public ResultBean removeTemplate(@RequestParam(value = "uid") String uid) {
        try {
            ocrTemplateService.removeTemplate(uid);
            return ResultBean.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "识别标注字段 Recognize Label Fields")
    @PostMapping(value = "/getLabelData", produces = "application/json;charset=utf-8")
    public ResultBean getLabelData(@RequestBody LabelDTO labelDTO) {
        try {
            String result = ocrTemplateService.getLabelData(labelDTO.getUid(), labelDTO.getLabelData());
            logger.info("LabelData: " + result);
            return ResultBean.success().add("result", result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "创建模板 Create Template")
    @PostMapping(value = "/addTemplate")
    public ResultBean addTemplate(@RequestParam(value = "name") String name, @RequestParam(value = "imageFile") MultipartFile imageFile) {
        try {
            // 要上传的目标文件存放路径
            // Target file storage path to be uploaded
            FileProperties.ElPath path = properties.getPath();
            String imagePath = path.getPath().replace("\\", "/") + "images/";
            FileUtils.checkAndCreatePath(imagePath);
            String templatePath = path.getPath().replace("\\", "/") + "templates/";
            FileUtils.checkAndCreatePath(templatePath);

            TemplateBean templateBean = new TemplateBean();
            String uid = UUIDUtils.getUUID();
            templateBean.setUid(uid);
            //image/jpg' || 'image/jpeg' || 'image/png'
            String suffix = FileUtils.getSuffix(imageFile.getOriginalFilename());
            if (!suffix.equalsIgnoreCase(".jpg") &&
                    !suffix.equalsIgnoreCase(".jpeg") &&
                    !suffix.equalsIgnoreCase(".png") &&
                    !suffix.equalsIgnoreCase(".bmp")) {
                return ResultBean.failure().add("errors", "图片格式应为: jpg, jpeg, png 或者 bmp!");
            }
            String imageName = FileUtils.getFileName(imageFile.getOriginalFilename());
            templateBean.setImageName(imageName);
            templateBean.setName(name);

            logger.info("Template name:" + name);

            if (FileUtils.upload(imageFile, imagePath, imageName)) {
                ocrTemplateService.addTemplate(templateBean);
                return ResultBean.success().add("result", templateBean);
            } else {
                return ResultBean.failure();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "模版文字识别-URL Template Text Recognition-URL")
    @GetMapping(value = "/infoForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean infoForImageUrl(@RequestParam(value = "uid") String uid, @RequestParam(value = "url") String url) {
        try {
//            TemplateBean templateBean = ocrTemplateService.getTemplate(uid);
            TemplateBean templateBean = ocrTemplateService.getTemplateRecInfo(uid);
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            Map<String, String> hashMap = ocrTemplateService.getImageInfo(templateBean, image);

            return ResultBean.success().add("result", hashMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "模版文字识别-图片 - Template Text Recognition-Image")
    @PostMapping(value = "/infoForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean infoForImageFile(@RequestParam(value = "uid") String uid, @RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
//            TemplateBean templateBean = ocrTemplateService.getTemplate(uid);
            TemplateBean templateBean = ocrTemplateService.getTemplateRecInfo(uid);
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            Map<String, String> hashMap = ocrTemplateService.getImageInfo(templateBean, image);

            return ResultBean.success().add("result", hashMap)
                    .add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }
}
