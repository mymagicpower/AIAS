package top.aias.platform.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.opencv.OpenCVImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.platform.bean.ResultBean;
import top.aias.platform.service.ImgColorService;
import top.aias.platform.service.ImgSrService;
import top.aias.platform.utils.FaceAlignUtils;
import top.aias.platform.utils.FaceUtils;
import top.aias.platform.utils.ImageUtils;
import top.aias.platform.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑白照片上色
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "黑白照片上色")
@RestController
@RequestMapping("/api/color")
public class ImgColorController {
    private Logger logger = LoggerFactory.getLogger(ImgColorController.class);

    @Autowired
    private ImgColorService imgColorService;

    @ApiOperation(value = "照片上色-URL")
    @GetMapping(value = "/imageColorForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageColorForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像上色
            Image img = imgColorService.colorize(image);
            Mat wrappedImage = (Mat) img.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "照片上色-图片")
    @PostMapping(value = "/imageColorForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageColorForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像上色
            Image segImg = imgColorService.colorize(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }
}
