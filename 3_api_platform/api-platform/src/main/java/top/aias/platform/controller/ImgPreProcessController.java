package top.aias.platform.controller;

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
import top.aias.platform.bean.ResultBean;
import top.aias.platform.service.ImgPreProcessService;
import top.aias.platform.utils.ImageUtils;
import top.aias.platform.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * 图像预处理，可配合图像生成使用
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "图像预处理")
@RestController
@RequestMapping("/api/preprocess")
public class ImgPreProcessController {
    private Logger logger = LoggerFactory.getLogger(ImgPreProcessController.class);

    @Autowired
    private ImgPreProcessService imgPreProcessService;

    @ApiOperation(value = "1. Canny 边缘检测 - URL")
    @GetMapping(value = "/imageCannyForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageCannyForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.canny(image);
            Mat wrappedImage = (Mat) img.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2ImageForSingleChannel(wrappedImage);
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

    @ApiOperation(value = "1. Canny 边缘检测 - 图片")
    @PostMapping(value = "/imageCannyForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageCannyForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.canny(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2ImageForSingleChannel(wrappedImage);
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

    @ApiOperation(value = "2. MLSD 线条检测 - URL")
    @GetMapping(value = "/imageMlsdForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageMlsdForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.mlsd(image);
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

    @ApiOperation(value = "2. MLSD 线条检测 - 图片")
    @PostMapping(value = "/imageMlsdForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageMlsdForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.mlsd(image);
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

    @ApiOperation(value = "3.1 Scribble 涂鸦 hed模型 - URL")
    @GetMapping(value = "/imageScribbleHedForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageScribbleHedForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.scribbleHed(image);
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

    @ApiOperation(value = "3.1 Scribble 涂鸦 hed模型 - 图片")
    @PostMapping(value = "/imageScribbleHedForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageScribbleHedForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.scribbleHed(image);
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

    @ApiOperation(value = "3.2 Scribble 涂鸦 Pidinet模型 - URL")
    @GetMapping(value = "/imageScribblePidiForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageScribblePidiForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.scribblePidinet(image);
            Mat wrappedImage = (Mat) img.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2ImageForSingleChannel(wrappedImage);
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

    @ApiOperation(value = "3.2 Scribble 涂鸦 Pidinet模型 - 图片")
    @PostMapping(value = "/imageScribblePidiForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageScribblePidiForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.scribblePidinet(image);
            Mat wrappedImage = (Mat) segImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2ImageForSingleChannel(wrappedImage);
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

    @ApiOperation(value = "4.1 SoftEdge 边缘检测hed模型 - URL")
    @GetMapping(value = "/imageSoftEdgeHedForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageSoftEdgeHedForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.softedgeHed(image);
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

    @ApiOperation(value = "4.1 SoftEdge 边缘检测 hed模型 - 图片")
    @PostMapping(value = "/imageSoftEdgeHedForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageSoftEdgeHedForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.softedgeHed(image);
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

    @ApiOperation(value = "4.2 SoftEdge 边缘检测 Pidinet模型 - URL")
    @GetMapping(value = "/imageSoftEdgePidiForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageSoftEdgePidiForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.softedgePidinet(image);
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

    @ApiOperation(value = "4.2 SoftEdge 边缘检测 Pidinet模型 - 图片")
    @PostMapping(value = "/imageSoftEdgePidiForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageSoftEdgePidiForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.softedgePidinet(image);
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

    @ApiOperation(value = "5. OpenPose 姿态检测 - URL")
    @GetMapping(value = "/imagePoseForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imagePoseForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.pose(image);
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

    @ApiOperation(value = "5. OpenPose 姿态检测 - 图片")
    @PostMapping(value = "/imagePoseForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imagePoseForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.pose(image);
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

    @ApiOperation(value = "6. Segmentation 语义分割 - URL")
    @GetMapping(value = "/imageSegForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageSegForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.segUpernet(image);
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

    @ApiOperation(value = "6. Segmentation 语义分割 - 图片")
    @PostMapping(value = "/imageSegForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageSegForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.segUpernet(image);
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

    @ApiOperation(value = "7.1 Depth 深度检测 DPT模型 - URL")
    @GetMapping(value = "/imageDepthDptForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageDepthDptForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.depthEstimationDpt(image);
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

    @ApiOperation(value = "7.1 Depth 深度检测 DPT模型 - 图片")
    @PostMapping(value = "/imageDepthDptForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageDepthDptForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.depthEstimationDpt(image);
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

    @ApiOperation(value = "7.2 Depth 深度检测 Midas模型 - URL")
    @GetMapping(value = "/imageDepthMidasForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageDepthMidasForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.depthEstimationMidas(image);
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

    @ApiOperation(value = "7.2 Depth 深度检测 Midas模型 - 图片")
    @PostMapping(value = "/imageDepthMidasForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageDepthMidasForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.depthEstimationMidas(image);
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

    @ApiOperation(value = "8. Normal Map 法线贴图 - URL")
    @GetMapping(value = "/imageNormalForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageNormalForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.normalBae(image);
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

    @ApiOperation(value = "8. Normal Map 法线贴图 - 图片")
    @PostMapping(value = "/imageNormalForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageNormalForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.normalBae(image);
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

    @ApiOperation(value = "9. Lineart 生成线稿 - URL")
    @GetMapping(value = "/imageLineartForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageLineartForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.lineart(image);
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

    @ApiOperation(value = "9. Lineart 生成线稿 - 图片")
    @PostMapping(value = "/imageLineartForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageLineartForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.lineart(image);
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

    @ApiOperation(value = "10. Lineart Anime 生成线稿 - URL")
    @GetMapping(value = "/imageLineartAnimeForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageLineartAnimeForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.lineartAnime(image);
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

    @ApiOperation(value = "10. Lineart Anime 生成线稿 - 图片")
    @PostMapping(value = "/imageLineartAnimeForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageLineartAnimeForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.lineartAnime(image);
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

    @ApiOperation(value = "11. Content Shuffle - URL")
    @GetMapping(value = "/imageShuffleForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageShuffleForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // Canny 边缘检测
            Image img = imgPreProcessService.contentShuffle(image);
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

    @ApiOperation(value = "11. Content Shuffle - 图片")
    @PostMapping(value = "/imageShuffleForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageShuffleForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny 边缘检测
            Image segImg = imgPreProcessService.contentShuffle(image);
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
