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
 * 图像高清处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Api(tags = "图像高清处理")
@RestController
@RequestMapping("/api/img")
public class ImgSrController {
    private Logger logger = LoggerFactory.getLogger(ImgSrController.class);

    @Autowired
    private ImgSrService imgService;

    @ApiOperation(value = "图像高清-URL")
    @GetMapping(value = "/imageSrForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean imageSrForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image img = imgService.imageSr(image);
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

    @ApiOperation(value = "图像高清-图片")
    @PostMapping(value = "/imageSrForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean imageSrForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image segImg = imgService.imageSr(image);
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

    @ApiOperation(value = "人像修复-URL")
    @GetMapping(value = "/faceResForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean faceResForImageUrl(@RequestParam(value = "url") String url) {
        try (NDManager manager = NDManager.newBaseManager()) {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 人脸检测
            DetectedObjects detections = imgService.faceDet(image);

            List<DetectedObjects.DetectedObject> items = detections.items();
            List<String> names = new ArrayList<>();
            List<Double> probs = new ArrayList<>();
            List<BoundingBox> landmarks = new ArrayList<>();
            int index = 0;
            for (DetectedObjects.DetectedObject item : items) {
                names.add(item.getClassName());
                probs.add(item.getProbability());
                landmarks.add(item.getBoundingBox()); // Landmark
                BoundingBox box = item.getBoundingBox();
                Rectangle rectangle = box.getBounds();

                // 人脸抠图
                // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
                Rectangle subImageRect =
                        FaceUtils.getSubImageRect(rectangle, image.getWidth(), image.getHeight(), 1.0f);
                int x = (int) (subImageRect.getX());
                int y = (int) (subImageRect.getY());
                int w = (int) (subImageRect.getWidth());
                int h = (int) (subImageRect.getHeight());
                Image subImage = image.getSubImage(x, y, w, h);

                // 保存，抠出的人脸图
//                ImageUtils.saveImage(subImage, "face_" + index + ".png", "build/output");

                // 获取人脸关键点列表
                List<Point> points = (List<Point>) box.getPath();

                // 计算人脸关键点在子图中的新坐标
                double[][] pointsArray = FaceUtils.facePoints(points);
                NDArray srcPoints = manager.create(pointsArray);
                NDArray dstPoints = FaceUtils.faceTemplate512x512(manager);

                // 定制的5点仿射变换 - Custom 5-point affine transformation
                Mat affine_matrix = OpenCVUtils.toOpenCVMat(manager, srcPoints, dstPoints);
                Mat mat = FaceAlignUtils.warpAffine((Mat) image.getWrappedImage(), affine_matrix);

                index++;

                Image alignedImg = OpenCVImageFactory.getInstance().fromImage(mat);
//                ImageUtils.saveImage(alignedImg, "face_align_" + index + ".png", "build/output");

                // 人脸修复
                Image restored_face = imgService.faceGan(alignedImg);
//                ImageUtils.saveImage(restored_face, "restored_face_" + index + ".png", "build/output");

                Mat inverse_affine = OpenCVUtils.invertAffineTransform(affine_matrix);

                Mat inv_restored = FaceAlignUtils.warpAffine((Mat) restored_face.getWrappedImage(), inverse_affine, image.getWidth(), image.getHeight());
                Image inv_restored_img = OpenCVImageFactory.getInstance().fromImage(inv_restored);
//                ImageUtils.saveImage(inv_restored_img, "inv_restored_" + index + ".png", "build/output");

                // 人脸分割
                Image faceParseImg = imgService.faceSeg(restored_face);
                Mat inv_soft_mask = FaceAlignUtils.warpAffine((Mat) faceParseImg.getWrappedImage(), inverse_affine, image.getWidth(), image.getHeight(), 3);
                Image inv_soft_mask_img = OpenCVImageFactory.getInstance().fromImage(inv_soft_mask);

                // upsample_img = inv_soft_mask * pasted_face + (1 - inv_soft_mask) * upsample_img
                NDArray inv_soft_mask_array = inv_soft_mask_img.toNDArray(manager).get(new NDIndex(":,:,0"));
                inv_soft_mask_array = inv_soft_mask_array.expandDims(2);
                byte[] a = inv_soft_mask_array.toByteArray();
                NDArray pasted_face_array = inv_restored_img.toNDArray(manager);
                NDArray image_array = image.toNDArray(manager);
                image_array = inv_soft_mask_array.mul(pasted_face_array).add(inv_soft_mask_array.neg().add(1).mul(image_array));
                image_array = image_array.toType(DataType.UINT8, false);
                image = OpenCVImageFactory.getInstance().fromNDArray(image_array);
//                ImageUtils.saveImage(image, "pasted_face_" + index + ".png", "build/output");
            }

//            ImageUtils.saveBoundingBoxImage(image, detections, "retinaface_detected.png", "build/output");
//            logger.info("{}", detections);

            Mat wrappedImage = (Mat) image.getWrappedImage();
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

    @ApiOperation(value = "人像修复-图片")
    @PostMapping(value = "/faceResForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean faceResForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream();
             NDManager manager = NDManager.newBaseManager()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 人脸检测
            DetectedObjects detections = imgService.faceDet(image);

            List<DetectedObjects.DetectedObject> items = detections.items();
            List<String> names = new ArrayList<>();
            List<Double> probs = new ArrayList<>();
            List<BoundingBox> landmarks = new ArrayList<>();
            int index = 0;
            for (DetectedObjects.DetectedObject item : items) {
                names.add(item.getClassName());
                probs.add(item.getProbability());
                landmarks.add(item.getBoundingBox()); // Landmark
                BoundingBox box = item.getBoundingBox();
                Rectangle rectangle = box.getBounds();

                // 人脸抠图
                // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
                Rectangle subImageRect =
                        FaceUtils.getSubImageRect(rectangle, image.getWidth(), image.getHeight(), 1.0f);
                int x = (int) (subImageRect.getX());
                int y = (int) (subImageRect.getY());
                int w = (int) (subImageRect.getWidth());
                int h = (int) (subImageRect.getHeight());
                Image subImage = image.getSubImage(x, y, w, h);

                // 保存，抠出的人脸图
//                ImageUtils.saveImage(subImage, "face_" + index + ".png", "build/output");

                // 获取人脸关键点列表
                List<Point> points = (List<Point>) box.getPath();

                // 计算人脸关键点在子图中的新坐标
                double[][] pointsArray = FaceUtils.facePoints(points);
                NDArray srcPoints = manager.create(pointsArray);
                NDArray dstPoints = FaceUtils.faceTemplate512x512(manager);

                // 定制的5点仿射变换 - Custom 5-point affine transformation
                Mat affine_matrix = OpenCVUtils.toOpenCVMat(manager, srcPoints, dstPoints);
                Mat mat = FaceAlignUtils.warpAffine((Mat) image.getWrappedImage(), affine_matrix);

                index++;

                Image alignedImg = OpenCVImageFactory.getInstance().fromImage(mat);
//                ImageUtils.saveImage(alignedImg, "face_align_" + index + ".png", "build/output");

                // 人脸修复
                Image restored_face = imgService.faceGan(alignedImg);
//                ImageUtils.saveImage(restored_face, "restored_face_" + index + ".png", "build/output");

                Mat inverse_affine = OpenCVUtils.invertAffineTransform(affine_matrix);

                Mat inv_restored = FaceAlignUtils.warpAffine((Mat) restored_face.getWrappedImage(), inverse_affine, image.getWidth(), image.getHeight());
                Image inv_restored_img = OpenCVImageFactory.getInstance().fromImage(inv_restored);
//                ImageUtils.saveImage(inv_restored_img, "inv_restored_" + index + ".png", "build/output");

                // 人脸分割
                Image faceParseImg = imgService.faceSeg(restored_face);
                Mat inv_soft_mask = FaceAlignUtils.warpAffine((Mat) faceParseImg.getWrappedImage(), inverse_affine, image.getWidth(), image.getHeight(), 3);
                Image inv_soft_mask_img = OpenCVImageFactory.getInstance().fromImage(inv_soft_mask);

                // upsample_img = inv_soft_mask * pasted_face + (1 - inv_soft_mask) * upsample_img
                NDArray inv_soft_mask_array = inv_soft_mask_img.toNDArray(manager).get(new NDIndex(":,:,0"));
                inv_soft_mask_array = inv_soft_mask_array.expandDims(2);
                byte[] a = inv_soft_mask_array.toByteArray();
                NDArray pasted_face_array = inv_restored_img.toNDArray(manager);
                NDArray image_array = image.toNDArray(manager);
                image_array = inv_soft_mask_array.mul(pasted_face_array).add(inv_soft_mask_array.neg().add(1).mul(image_array));
                image_array = image_array.toType(DataType.UINT8, false);
                image = OpenCVImageFactory.getInstance().fromNDArray(image_array);
//                ImageUtils.saveImage(image, "pasted_face_" + index + ".png", "build/output");
            }

//            ImageUtils.saveBoundingBoxImage(image, detections, "retinaface_detected.png", "build/output");
//            logger.info("{}", detections);

            Mat wrappedImage = (Mat) image.getWrappedImage();
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

    @ApiOperation(value = "头像高清-URL")
    @GetMapping(value = "/faceGanForImageUrl", produces = "application/json;charset=utf-8")
    public ResultBean generalSegSmallForImageUrl(@RequestParam(value = "url") String url) {
        try {
            Image image = OpenCVImageFactory.getInstance().fromUrl(url);
            // 图像分割
            Image img = imgService.faceGan(image);
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

    @ApiOperation(value = "头像高清-图片")
    @PostMapping(value = "/faceGanForImageFile", produces = "application/json;charset=utf-8")
    public ResultBean faceGanForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        try (InputStream inputStream = imageFile.getInputStream()) {
//            String base64Img = Base64.encodeBase64String(imageFile.getBytes());
            Image image = OpenCVImageFactory.getInstance().fromInputStream(inputStream);
            // 图像分割
            Image img = imgService.faceGan(image);
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
}
