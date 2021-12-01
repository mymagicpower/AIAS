package me.aias.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.aias.service.OcrService;
import org.apache.commons.codec.binary.Base64;
import me.aias.domain.DataBean;
import me.aias.domain.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Api(tags = "文字识别")
@RestController
@RequestMapping("/ocr")
public class OcrController {
    private Logger logger = LoggerFactory.getLogger(OcrController.class);

    @Autowired
    private OcrService inferService;

    @ApiOperation(value = "通用文字识别-URL")
    @GetMapping(value = "/generalInfoForImageUrl")
    public ResultBean generalInfoForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        List<DataBean> dataList = inferService.getGeneralInfo(image);
        return ResultBean.success().add("result", dataList);
    }

    @ApiOperation(value = "通用文字识别-图片")
    @PostMapping("/generalInfoForImageFile")
    public ResultBean generalInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            List<DataBean> dataList = inferService.getGeneralInfo(image);
            
            return ResultBean.success().add("result", dataList)
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
}
