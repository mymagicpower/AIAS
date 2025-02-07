package top.aias.seg;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.UNetModel;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * u2net：通用的的预训练模型，通常用这个就行。
 * u2netp：u2net的轻量级版本。
 * silueta：和u2net相同，但是大小减少到43Mb，方便在小内存机器上使用。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class RemBgExample {

    private static final Logger logger = LoggerFactory.getLogger(RemBgExample.class);

    private RemBgExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/bbb.png"); // aaa.jpg
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        // u2net.onnx
        // u2netp.onnx
        // silueta.onnx
        try (UNetModel model = new UNetModel("models/", "u2netp.onnx", 1, false, true, Device.cpu())) {
            Image segImg = model.predict(img);
            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) segImg.getWrappedImage());
            BufferedImage bImg = ImageUtils.removeBg(bufferedImage);
            ImageUtils.saveBufferedImage(bImg, "u2netp_seg.png", "build/output");
        }
    }
}
