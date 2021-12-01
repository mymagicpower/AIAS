package me.aias.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import me.aias.domain.DataBean;
import me.aias.domain.ResultBean;
import me.aias.service.FaceService;
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
@Api(tags = "人脸识别")
@RestController
@RequestMapping("/face")
public class FaceController {
    private Logger logger = LoggerFactory.getLogger(FaceController.class);

    @Autowired
    private FaceService faceService;

    @ApiOperation(value = "人脸检测-URL")
    @GetMapping(value = "/faceDetectionForImageUrl")
    public ResultBean generalInfoForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        List<DataBean> dataList = faceService.detect(image);
        return ResultBean.success().add("result", dataList);
    }

    @ApiOperation(value = "人脸检测-图片")
    @PostMapping("/faceDetectionForImageFile")
    public ResultBean generalInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            List<DataBean> dataList = faceService.detect(image);

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

    @ApiOperation(value = "人脸特征提取-URL")
    @GetMapping(value = "/featureForImageUrl")
    public ResultBean featureForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        float[] feature = faceService.feature(image);
        return ResultBean.success().add("result", feature);
    }

    @ApiOperation(value = "人脸特征提取-图片")
    @PostMapping("/featureForImageFile")
    public ResultBean featureForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            float[] feature = faceService.feature(image);

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

    @ApiOperation(value = "人脸比对(1:1)-URL")
    @GetMapping(value = "/compareForImageUrls")
    public ResultBean compareForImageUrls(@RequestParam(value = "url1") String url1, @RequestParam(value = "url2") String url2) throws IOException, ModelException, TranslateException {
        Image image1 = ImageFactory.getInstance().fromUrl(url1);
        Image image2 = ImageFactory.getInstance().fromUrl(url2);
        String result = faceService.compare(image1, image2);
        return ResultBean.success().add("result", result);
    }

    @ApiOperation(value = "人脸比对(1:1)-图片")
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

            String result = faceService.compare(image1, image2);

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
