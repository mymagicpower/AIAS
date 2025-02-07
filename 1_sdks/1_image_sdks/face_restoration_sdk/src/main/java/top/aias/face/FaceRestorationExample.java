package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
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
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.face.det.FaceDetModel;
import top.aias.face.gan.FaceGanModel;
import top.aias.face.seg.FaceSegModel;
import top.aias.face.util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 人脸修复
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */public final class FaceRestorationExample {

    private static final Logger logger = LoggerFactory.getLogger(FaceRestorationExample.class);

    private FaceRestorationExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path facePath = Paths.get("src/test/resources/beauty.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(facePath);

        try (FaceGanModel faceModel = new FaceGanModel("models/", "gfpgan_traced_model.pt", 1, Device.cpu());
             FaceSegModel faceSegModel = new FaceSegModel("models/", "parsenet_traced_model.pt", 1, Device.cpu());
             FaceDetModel faceDetModel = new FaceDetModel("models/", "retinaface_traced_model.pt", 1, Device.cpu());
             NDManager manager = NDManager.newBaseManager()) {

            DetectedObjects detections = faceDetModel.predict(image);

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
                // Crop the face image
                // factor = 0.1f, 意思是扩大10%，防止人脸仿射变换后，人脸被部分截掉
                // factor = 0.1f, meaning to expand by 10%, to prevent the face from being partially cut off after affine transformation
                Rectangle subImageRect =
                        FaceUtils.getSubImageRect(rectangle, image.getWidth(), image.getHeight(), 1.0f);
                int x = (int) (subImageRect.getX());
                int y = (int) (subImageRect.getY());
                int w = (int) (subImageRect.getWidth());
                int h = (int) (subImageRect.getHeight());
                Image subImage = image.getSubImage(x, y, w, h);

                // 保存，抠出的人脸图
                // Save the cropped face image
                ImageUtils.saveImage(subImage, "face_" + index + ".png", "build/output");

                // 获取人脸关键点列表
                // Get the list of face keypoints
                List<Point> points = (List<Point>) box.getPath();

                // 计算人脸关键点在子图中的新坐标
                // Calculate the new coordinates of facial keypoints in the sub-image
                double[][] pointsArray = FaceUtils.facePoints(points);
                NDArray srcPoints = manager.create(pointsArray);
                NDArray dstPoints = FaceUtils.faceTemplate512x512(manager);

                // 定制的5点仿射变换 - Custom 5-point affine transformation
                Mat affine_matrix = OpenCVUtils.toOpenCVMat(manager, srcPoints, dstPoints);
                Mat mat = FaceAlignUtils.warpAffine((Mat) image.getWrappedImage(), affine_matrix);

                index++;

                Image alignedImg = OpenCVImageFactory.getInstance().fromImage(mat);
                ImageUtils.saveImage(alignedImg, "face_align_" + index + ".png", "build/output");

                Image restored_face = faceModel.predict(alignedImg);
                ImageUtils.saveImage(restored_face, "restored_face_" + index + ".png", "build/output");

                Mat inverse_affine = OpenCVUtils.invertAffineTransform(affine_matrix);

                Mat inv_restored = FaceAlignUtils.warpAffine((Mat) restored_face.getWrappedImage(), inverse_affine, image.getWidth(), image.getHeight());
                Image inv_restored_img = OpenCVImageFactory.getInstance().fromImage(inv_restored);
                ImageUtils.saveImage(inv_restored_img, "inv_restored_" + index + ".png", "build/output");

                // mask = cv2.warpAffine(mask, inverse_affine, (w_up, h_up), flags=3)
                Image faceParseImg = faceSegModel.predict(restored_face);
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
                ImageUtils.saveImage(image, "pasted_face_" + index + ".png", "build/output");
            }

            ImageUtils.saveBoundingBoxImage(image, detections, "retinaface_detected.png", "build/output");
            logger.info("{}", detections);
        }
    }
}
