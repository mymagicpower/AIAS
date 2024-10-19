package top.aias.img.controller;

import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.img.bean.ResultBean;
import top.aias.img.service.SegService;
import top.aias.img.utils.ImageUtils;
import top.aias.img.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * 图像分割处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "图像分割处理")
@RestController
@RequestMapping("/seg")
public class SegController {
    private Logger logger = LoggerFactory.getLogger(SegController.class);

    @Autowired
    private SegService segService;

    @ApiOperation(value = "通用图像分割处理-大模型-URL")
    @GetMapping(value = "/generalSegBigForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalSegBigForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.generalSegBig(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用图像分割处理-大模型-图片")
    @PostMapping(value = "/generalSegBigForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean generalSegBigForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.generalSegBig(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用图像分割处理-中模型-URL")
    @GetMapping(value = "/generalSegMidForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalSegMidForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.generalSegMid(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用图像分割处理-中模型-图片")
    @PostMapping(value = "/generalSegMidForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean generalSegMidForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.generalSegMid(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用图像分割处理-小模型-URL")
    @GetMapping(value = "/generalSegSmallForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalSegSmallForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.generalSegSmall(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用图像分割处理-小模型-图片")
    @PostMapping(value = "/generalSegSmallForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean generalSegSmallForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.generalSegSmall(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "人体图像分割处理-URL")
    @GetMapping(value = "/humanSegForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean humanSegForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.humanSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "人体图像分割处理-图片")
    @PostMapping(value = "/humanSegForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean humanSegForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.humanSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "动漫图像分割处理-URL")
    @GetMapping(value = "/animeSegForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean animeSegForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.animeSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "动漫图像分割处理-图片")
    @PostMapping(value = "/animeSegForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean animeSegForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.animeSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "衣服图像分割处理-URL")
    @GetMapping(value = "/clothSegForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean clothSegForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image segImg = segService.clothSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "衣服图像分割处理-图片")
    @PostMapping(value = "/clothSegForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean clothSegForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = segService.clothSeg(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 背景透明处理
            BufferedImage removeBgImg = ImageUtils.removeBg(bufferedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(removeBgImg, "png");
            wrappedImage.release();
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }
}
