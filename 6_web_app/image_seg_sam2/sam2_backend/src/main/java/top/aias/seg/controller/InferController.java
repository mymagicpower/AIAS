package top.aias.seg.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.seg.bean.LabelDTO;
import top.aias.seg.bean.ResultBean;
import top.aias.seg.bean.ImageBean;
import top.aias.seg.configuration.FileProperties;
import top.aias.seg.service.ImageService;
import top.aias.seg.utils.FileUtils;
import top.aias.seg.utils.UUIDUtils;

/**
 * 框选抠图
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "框选抠图")
@RestController
@Configuration
@RequestMapping("/infer")
public class InferController {
    private Logger logger = LoggerFactory.getLogger(InferController.class);

    @Autowired
    private ImageService imageService;

    /**
     * 文件配置
     * File Configuration
     */
    @Autowired
    private FileProperties properties;

    @ApiOperation(value = "识别框选区")
    @PostMapping(value = "/getLabelData", produces = "application/json;charset=utf-8")
    public ResultBean getLabelData(@RequestBody LabelDTO labelDTO) {
        try {
            String base64Img = imageService.getLabelData(labelDTO.getUid(), labelDTO.getLabelData());
//            logger.info("LabelData: " + base64Img);
            return ResultBean.success().add("result", "data:imageName/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/uploadImage")
    public ResultBean uploadImage(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try {
            // 要上传的目标文件存放路径
            // Target file storage path to be uploaded
            FileProperties.ElPath path = properties.getPath();
            String imagePath = path.getPath().replace("\\", "/") + "images/";
            FileUtils.checkAndCreatePath(imagePath);
            String templatePath = path.getPath().replace("\\", "/") + "templates/";
            FileUtils.checkAndCreatePath(templatePath);

            ImageBean imageBean = new ImageBean();
            String uid = UUIDUtils.getUUID();
            imageBean.setUid(uid);
            //image/jpg' || 'image/jpeg' || 'image/png'
            String suffix = FileUtils.getSuffix(imageFile.getOriginalFilename());
            if (!suffix.equalsIgnoreCase(".jpg") &&
                    !suffix.equalsIgnoreCase(".jpeg") &&
                    !suffix.equalsIgnoreCase(".png") &&
                    !suffix.equalsIgnoreCase(".bmp")) {
                return ResultBean.failure().add("errors", "图片格式应为: jpg, jpeg, png 或者 bmp!");
            }
            String imageName = FileUtils.getFileName(imageFile.getOriginalFilename());
            imageBean.setImageName(imageName);
            imageBean.setName("");

            if (FileUtils.upload(imageFile, imagePath, imageName)) {
                imageService.addImageInfo(imageBean);
                return ResultBean.success().add("result", imageBean);
            } else {
                return ResultBean.failure();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

}
