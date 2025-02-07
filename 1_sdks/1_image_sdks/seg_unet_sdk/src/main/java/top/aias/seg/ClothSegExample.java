package top.aias.seg;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.UNetClothSegModel;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * u2net_cloth_seg：专门从人像上抠衣服的预训练模型，它会把衣服分成三部分：上半身、下半身和全身。
 * 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class ClothSegExample {

    private static final Logger logger = LoggerFactory.getLogger(ClothSegExample.class);

    private ClothSegExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/fullbody2.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        // clothCategory 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
        try (UNetClothSegModel detector = new UNetClothSegModel("models/", "cloth.onnx", 4, 1, false, Device.cpu())) {
            Image segImg = detector.predict(img);
            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) segImg.getWrappedImage());
            bufferedImage = ImageUtils.removeBg(bufferedImage);
            ImageUtils.saveBufferedImage(bufferedImage, "u2net_cloth_seg.png", "build/output");
        }
    }
}
