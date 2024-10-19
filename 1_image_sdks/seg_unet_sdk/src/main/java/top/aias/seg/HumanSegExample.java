package top.aias.seg;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.UNetHumanSegModel;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * u2net_human_seg：专门针对人像分割的预训练模型，只是分割人像时建议使用。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class HumanSegExample {

    private static final Logger logger = LoggerFactory.getLogger(HumanSegExample.class);

    private HumanSegExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/pose.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (UNetHumanSegModel detector = new UNetHumanSegModel("models/", "human.onnx", 1, false, false, Device.cpu())) {
            Image segImg = detector.predict(img);
            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) segImg.getWrappedImage());
            bufferedImage = ImageUtils.removeBg(bufferedImage);
            ImageUtils.saveBufferedImage(bufferedImage, "u2net_human_seg.png", "build/output");
        }
    }
}
