package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.aias.config.FileProperties;
import me.aias.domain.ResultBean;
import me.aias.service.InferService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "在线推理")
@RequestMapping("/api/inference")
public class InferController {
    private Logger logger = LoggerFactory.getLogger(InferController.class);

    private final FileProperties properties;
    
    @Autowired
    private InferService inferService;

    @ApiOperation(value = "图片分类识别-URL")
    @GetMapping(value = "/classInfoForUrl", produces = "application/json;charset=utf-8")
    public ResultBean getClassInfoForUrl(@RequestParam(value = "url") String url) {
//        properties.getPath().getPath() + type + File.separator

        String result = inferService.getClassificationInfoForUrl(properties.getPath().getNewModelPath(), url);
        return ResultBean.success().add("result", result);
    }

    @ApiOperation(value = "图片分类识别-图片")
    @PostMapping("/classInfoForImage")
    public ResultBean getClassInfo(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream ins = imageFile.getInputStream();
            String result = inferService.getClassificationInfo(properties.getPath().getNewModelPath(), ins);
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("result", result)
                    .add("base64Img", "data:image/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "图片特征提取-URL")
    @GetMapping(value = "/featureForImageUrl")
    public ResultBean featureForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        float[] feature = inferService.feature(properties.getPath().getNewModelPath(),image);
        return ResultBean.success().add("result", feature);
    }

    @ApiOperation(value = "图片特征提取-图片")
    @PostMapping("/featureForImageFile")
    public ResultBean featureForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            float[] feature = inferService.feature(properties.getPath().getNewModelPath(),image);

            return ResultBean.success().add("result", feature)
                    .add("base64Img", "data:image/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "图片比对(1:1)-URL")
    @GetMapping(value = "/compareForImageUrls")
    public ResultBean compareForImageUrls(@RequestParam(value = "url1") String url1, @RequestParam(value = "url2") String url2) throws IOException, TranslateException, ModelException {
        Image image1 = ImageFactory.getInstance().fromUrl(url1);
        Image image2 = ImageFactory.getInstance().fromUrl(url2);
        String result = inferService.compare(properties.getPath().getNewModelPath(),image1, image2);
        return ResultBean.success().add("result", result);
    }

    @ApiOperation(value = "图片比对(1:1)-图片")
    @PostMapping("/compareForImageFiles")
    public ResultBean compareForImageFiles(@RequestParam(value = "imageFiles") MultipartFile[] imageFiles) {
        InputStream fis = null;
        try {
            InputStream inputStream1 = imageFiles[0].getInputStream();
            String base64Img1 = Base64.encodeBase64String(imageFiles[0].getBytes());
            Image image1 = ImageFactory.getInstance().fromInputStream(inputStream1);

            InputStream inputStream2 = imageFiles[1].getInputStream();
            String base64Img2 = Base64.encodeBase64String(imageFiles[1].getBytes());
            Image image2 = ImageFactory.getInstance().fromInputStream(inputStream2);

            String result = inferService.compare(properties.getPath().getNewModelPath(),image1, image2);

            return ResultBean.success().add("result", result)
                    .add("base64Img1", "data:image/jpeg;base64," + base64Img1).add("base64Img2", "data:image/jpeg;base64," + base64Img2);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
