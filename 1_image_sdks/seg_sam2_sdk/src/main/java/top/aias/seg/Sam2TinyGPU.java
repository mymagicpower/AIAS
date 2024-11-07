package top.aias.seg;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Mask;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.Sam2DecoderModel;
import top.aias.seg.model.Sam2EncoderModel;
import top.aias.seg.model.Sam2Input;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Sam2图像分割
 * 提供2个模型，支持CPU,GPU
 * sam2-hiera-large
 * sam2-hiera-tiny
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Sam2TinyGPU {

    private static final Logger logger = LoggerFactory.getLogger(Sam2TinyGPU.class);

    private Sam2TinyGPU() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/truck.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        Sam2Input input = Sam2Input.builder(image).addPoint(575, 750).addBox(425, 600, 700, 875).build();

        try (Sam2EncoderModel encoderModel = new Sam2EncoderModel("models/", "sam2-hiera-tiny-encoder.onnx", 1, Device.gpu());
             Sam2DecoderModel decoderModel = new Sam2DecoderModel("models/", "sam2-hiera-tiny-decoder.onnx", 1, Device.gpu());
             NDManager manager = NDManager.newBaseManager(Device.gpu(), "PyTorch");) {
            NDList embeddings = encoderModel.predict(input);
            embeddings.attach(manager);
            input.setEmbeddings(embeddings);
            DetectedObjects detection = decoderModel.predict(input);

            // 抠图
            matting(manager, input, detection, true);

            // 显示遮罩层
            showMask(input, detection);

            logger.info("{}", detection);
        }
    }

    private static void showMask(Sam2Input input, DetectedObjects detection) throws IOException {
        Image img = input.getImage();
        img.drawBoundingBoxes(detection, 0.8f);
        img.drawMarks(input.getPoints());
        for (Rectangle rect : input.getBoxes()) {
            OpenCVUtils.drawRectangle((Mat) img.getWrappedImage(), rect, 0xff0000, 6);
        }

        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        Path imagePath = outputDir.resolve("sam2.png");
        img.save(Files.newOutputStream(imagePath), "png");
    }

    private static void matting(NDManager manager, Sam2Input input, DetectedObjects detection, boolean gaussianBlur) throws IOException {
        Image img = input.getImage();
        List<DetectedObjects.DetectedObject> list = detection.items();
        if (list.size() == 0)
            return;

        DetectedObjects.DetectedObject result = list.get(0);
        BoundingBox box = result.getBoundingBox();
        if (box instanceof Mask) {
            Mask mask = (Mask) box;
            float[][] probDist = mask.getProbDist();
            NDArray oriImgArray = img.toNDArray(manager, Image.Flag.COLOR);
            oriImgArray = oriImgArray.transpose(2, 0, 1);
            NDArray pred = manager.create(probDist);

            if (gaussianBlur) {
                pred = OpenCVUtils.gaussianBlur(manager, pred);
            }
            pred = pred.expandDims(0);
            pred = pred.concat(pred, 0).concat(pred, 0);

            // 黑色为 0， 白色 255
            oriImgArray = oriImgArray.mul(pred);
            Image newImg = ImageFactory.getInstance().fromNDArray(oriImgArray);

            Rectangle rect = input.getBoxes().get(0);
            newImg = newImg.getSubImage((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getWidth());

            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) newImg.getWrappedImage());
            bufferedImage = ImageUtils.removeBg(bufferedImage);

            Path outputDir = Paths.get("build/output");
            Files.createDirectories(outputDir);
            ImageUtils.saveBufferedImage(bufferedImage, "img_seg.png", outputDir.toString());
        }
    }
}
