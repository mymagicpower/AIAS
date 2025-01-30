package top.aias.training.controller;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import top.aias.training.config.FileProperties;
import top.aias.training.domain.ResultBean;
import top.aias.training.domain.TrainArgument;
import top.aias.training.service.InferService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.aias.training.service.TrainArgumentService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推理服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inference")
public class InferController {
    private Logger logger = LoggerFactory.getLogger(InferController.class);

    private final FileProperties properties;

    @Autowired
    private InferService inferService;
    @Autowired
    private TrainArgumentService trainArgumentService;

    @GetMapping(value = "/classInfoForUrl", produces = "application/json;charset=utf-8")
    public ResultBean getClassInfoForUrl(@RequestParam(value = "url") String url) {
//        properties.getPath().getPath() + type + File.separator
        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        String labels = trainArgument.getClassLabels();
        // 还原回 List<String>
        List<String> labelsList = Arrays.stream(labels.substring(1, labels.length() - 1) // 去除方括号
                        .split(", ")) // 以 ", " 分割
                .map(String::trim) // 去除多余空格
                .collect(Collectors.toList());

        String result = inferService.getClassificationInfoForUrl(properties.getPath().getSavePath(), url, labelsList);
        return ResultBean.success().add("result", result);
    }

    @PostMapping("/classInfoForImage")
    public ResultBean getClassInfo(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        String labels = trainArgument.getClassLabels();
        // 还原回 List<String>
        List<String> labelsList = Arrays.stream(labels.substring(1, labels.length() - 1) // 去除方括号
                        .split(", ")) // 以 ", " 分割
                .map(String::trim) // 去除多余空格
                .collect(Collectors.toList());

        InputStream fis = null;
        try {
            InputStream ins = imageFile.getInputStream();
            String result = inferService.getClassificationInfo(properties.getPath().getSavePath(), ins, labelsList);
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

    @GetMapping(value = "/featureForImageUrl")
    public ResultBean featureForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        float[] feature = inferService.feature(properties.getPath().getSavePath(),image);
        return ResultBean.success().add("result", feature);
    }

    @PostMapping("/featureForImageFile")
    public ResultBean featureForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream fis = null;
        try {
            InputStream inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            float[] feature = inferService.feature(properties.getPath().getSavePath(),image);

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

    //@ApiOperation(value = "Image Comparison (1:1)-URL")
    @GetMapping(value = "/compareForImageUrls")
    public ResultBean compareForImageUrls(@RequestParam(value = "url1") String url1, @RequestParam(value = "url2") String url2) throws IOException, TranslateException, ModelException {
        Image image1 = ImageFactory.getInstance().fromUrl(url1);
        Image image2 = ImageFactory.getInstance().fromUrl(url2);
        String result = inferService.compare(properties.getPath().getSavePath(),image1, image2);
        return ResultBean.success().add("result", result);
    }

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

            String result = inferService.compare(properties.getPath().getSavePath(),image1, image2);

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
