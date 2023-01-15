package me.aias.ocr.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.aias.ocr.configuration.FileProperties;
import me.aias.ocr.model.DataBean;
import me.aias.ocr.model.ResultBean;
import me.aias.ocr.service.InferService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Api(tags = "通用文字识别")
@RestController
@RequestMapping("/inference")
public class InferController {
    private Logger logger = LoggerFactory.getLogger(InferController.class);

    @Autowired
    private InferService inferService;

    @Value("${server.baseUri}")
    private String baseUri;

    /**
     * file configuration
     */
    @Autowired
    private FileProperties properties;

    @ApiOperation(value = "通用文字识别-URL")
    @GetMapping(value = "/generalInfoForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalInfoForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        try {
            Image image = ImageFactory.getInstance().fromUrl(url);
            List<DataBean> dataList = inferService.getGeneralInfo(image);
            return ResultBean.success().add("result", dataList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }

    @ApiOperation(value = "通用文字识别-图片")
    @PostMapping(value = "/generalInfoForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean generalInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            List<DataBean> dataList = inferService.getGeneralInfo(image);

            return ResultBean.success().add("result", dataList)
                    .add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("errors", e.getMessage());
        }
    }
}
