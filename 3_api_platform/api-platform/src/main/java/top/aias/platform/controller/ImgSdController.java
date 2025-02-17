package top.aias.platform.controller;

import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.google.api.client.util.Base64;
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
import top.aias.platform.service.ImgPreProcessService;
import top.aias.platform.service.ImgSdService;
import top.aias.platform.utils.ImageUtils;
import top.aias.platform.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Stable Diffusion 图像生成
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "图像生成")
@RestController
@RequestMapping("/api/sd")
public class ImgSdController {
    private Logger logger = LoggerFactory.getLogger(ImgSdController.class);

    @Autowired
    private ImgSdService imgSdService;

    @Autowired
    private ImgPreProcessService imgPreProcessService;

    @ApiOperation(value = "文生图")
    @GetMapping(value = "/txt2Image", produces = "application/json;charset=utf-8")
    public ResultBean txt2Image(@RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try {
            Image newImg = imgSdService.txt2Image(prompt, negativePrompt, Integer.parseInt(steps.trim()));

            Mat wrappedImage = (Mat) newImg.getWrappedImage();
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

    @ApiOperation(value = "图生图")
    @PostMapping(value = "/image2Image", produces = "application/json;charset=utf-8")
    public ResultBean image2Image(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            Image newImg = imgSdService.image2Image(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - Canny")
    @PostMapping(value = "/sdCanny", produces = "application/json;charset=utf-8")
    public ResultBean sdCanny(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // Canny
            org.opencv.core.Mat mat = OpenCVUtils.canny((org.opencv.core.Mat) image.getWrappedImage());
            image = OpenCVImageFactory.getInstance().fromImage(mat);

            Image newImg = imgSdService.sdCanny(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdDepth")
    @PostMapping(value = "/sdDepth", produces = "application/json;charset=utf-8")
    public ResultBean sdDepth(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);

            // TODO 支持两种 DPT Midas
            Image depthImg = imgPreProcessService.depthEstimationDpt(image);
//            Image depthImg = imgPreProcessService.depthEstimationMidas(image);
            Image newImg = imgSdService.sdDepth(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdLineartAnime")
    @PostMapping(value = "/sdLineartAnime", produces = "application/json;charset=utf-8")
    public ResultBean sdLineartAnime(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.lineartAnime(image);

            Image newImg = imgSdService.sdLineartAnime(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdLineart")
    @PostMapping(value = "/sdLineart", produces = "application/json;charset=utf-8")
    public ResultBean sdLineart(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // TODO 支持两种 lineart lineartCoarse
            image = imgPreProcessService.lineart(image);

            Image newImg = imgSdService.sdLineart(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdMlsd")
    @PostMapping(value = "/sdMlsd", produces = "application/json;charset=utf-8")
    public ResultBean sdMlsd(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.mlsd(image);

            Image newImg = imgSdService.sdMlsd(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdNormalBae")
    @PostMapping(value = "/sdNormalBae", produces = "application/json;charset=utf-8")
    public ResultBean sdNormalBae(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.normalBae(image);

            Image newImg = imgSdService.sdNormalBae(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdOpenPose")
    @PostMapping(value = "/sdOpenPose", produces = "application/json;charset=utf-8")
    public ResultBean sdOpenPose(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.pose(image);

            Image newImg = imgSdService.sdOpenPose(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdP2P")
    @PostMapping(value = "/sdP2P", produces = "application/json;charset=utf-8")
    public ResultBean sdP2P(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);

            Image newImg = imgSdService.sdP2P(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdScribble")
    @PostMapping(value = "/sdScribble", produces = "application/json;charset=utf-8")
    public ResultBean sdScribble(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // TODO 支持两种
            image = imgPreProcessService.scribbleHed(image);

            Image newImg = imgSdService.sdScribble(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdSeg")
    @PostMapping(value = "/sdSeg", produces = "application/json;charset=utf-8")
    public ResultBean sdSeg(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.segUpernet(image);

            Image newImg = imgSdService.sdSeg(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdShuffle")
    @PostMapping(value = "/sdShuffle", produces = "application/json;charset=utf-8")
    public ResultBean sdShuffle(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            image = imgPreProcessService.contentShuffle(image);

            Image newImg = imgSdService.sdShuffle(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "Control - sdSoftEdge")
    @PostMapping(value = "/sdSoftEdge", produces = "application/json;charset=utf-8")
    public ResultBean sdSoftEdge(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "prompt") String prompt, @RequestParam(value = "negativePrompt") String negativePrompt, @RequestParam(value = "steps") String steps) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            //TODO 支持两种
            image = imgPreProcessService.softedgeHed(image);

            Image newImg = imgSdService.sdSoftEdge(image, prompt, negativePrompt, Integer.parseInt(steps.trim()));
            Mat wrappedImage = (Mat) newImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            // 转 base64格式
            String base64Img = ImageUtils.toBase64(bufferedImage, "png");
            wrappedImage.release();

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            return ResultBean.success().add("base64Img", "data:image/png;base64," + base64Img).add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }
}
