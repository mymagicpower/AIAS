package top.aias.ocr.controller;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import com.google.api.client.util.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.aias.ocr.bean.DataBean;
import top.aias.ocr.bean.Point;
import top.aias.ocr.bean.ResultBean;
import top.aias.ocr.bean.RotatedBox;
import top.aias.ocr.service.InferService;
import top.aias.ocr.utils.ImageUtils;
import top.aias.ocr.utils.OpenCVUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文字识别
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "通用文字识别 -General Text Recognition")
@RestController
@RequestMapping("/inference")
public class InferController {
    private Logger logger = LoggerFactory.getLogger(InferController.class);

    @Autowired
    private InferService inferService;

    @Value("${server.baseUri}")
    private String baseUri;

    @ApiOperation(value = "通用文字识别-URL -General Text Recognition-URL")
    @GetMapping(value = "/generalInfoForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalInfoForImageUrl(@RequestParam(value = "url") String url) {
        try(NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            List<RotatedBox> detections = inferService.getGeneralInfo(manager, image);
            List<DataBean> dataList = this.getDataList(detections);

            // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
            Mat wrappedImage = (Mat) image.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            for (RotatedBox result : detections) {
                ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
            }
            String base64Img = ImageUtils.toBase64(bufferedImage, "jpg");

            return ResultBean.success().add("result", dataList).add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "通用文字识别-图片 -General Text Recognition-Image")
    @PostMapping(value = "/generalInfoForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean generalInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            List<RotatedBox> detections = inferService.getGeneralInfo(manager, image);
            List<DataBean> dataList = this.getDataList(detections);

            // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
            Mat wrappedImage = (Mat) image.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            for (RotatedBox result : detections) {
                ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
            }
            String base64Img = ImageUtils.toBase64(bufferedImage, "jpg");

            return ResultBean.success().add("result", dataList).add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "转正对齐-URL")
    @GetMapping(value = "/mlsdForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean mlsdForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            Image warpImg = inferService.getWarpImg(image);
            BufferedImage buffImage = OpenCVUtils.mat2Image((Mat) warpImg.getWrappedImage());
            String base64Img = ImageUtils.toBase64(buffImage,"jpg");
            return ResultBean.success().add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    @ApiOperation(value = "转正对齐-图片")
    @PostMapping(value = "/mlsdForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean mlsdForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            Image warpImg = inferService.getWarpImg(image);
            BufferedImage buffImage = OpenCVUtils.mat2Image((Mat) warpImg.getWrappedImage());

            String orgBase64Img = Base64.encodeBase64String(imageFile.getBytes());
            String base64Img = ImageUtils.toBase64(buffImage,"jpg");
            return ResultBean.success().add("orgBase64Img", "data:imageName/jpeg;base64," + orgBase64Img).add("base64Img", "data:imageName/jpeg;base64," + base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBean.failure().add("message", e.getMessage());
        }
    }

    private List<DataBean> getDataList(List<RotatedBox> detections){
        List<DataBean> dataList = new ArrayList<>();

        for (RotatedBox rotatedBox : detections) {
            DataBean dataBean = new DataBean();
            List<Point> points = new ArrayList<>();
            dataBean.setValue(rotatedBox.getText());

            float[] pointsArr = rotatedBox.getBox().toFloatArray();
            for (int i = 0; i < 4; i++) {
                Point point = new Point((int) pointsArr[2 * i], (int) pointsArr[2 * i + 1]);
                points.add(point);
            }

            dataBean.setPoints(points);
            dataList.add(dataBean);
        }
        return dataList;
    }
}
