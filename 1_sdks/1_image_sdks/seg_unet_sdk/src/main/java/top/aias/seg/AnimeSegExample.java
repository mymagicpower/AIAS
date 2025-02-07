package top.aias.seg;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.IsNetModel;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 动漫人物分割
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class AnimeSegExample {

    private static final Logger logger = LoggerFactory.getLogger(AnimeSegExample.class);

    private AnimeSegExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/anime-girl.jpg");
        Image img = BufferedImageFactory.getInstance().fromFile(imageFile);

        try (IsNetModel isNetModel = new IsNetModel("models/", "anime.onnx", 1, false, false, Device.cpu())) {
            Image segImg = isNetModel.predict(img);
            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) segImg.getWrappedImage());
            bufferedImage = ImageUtils.removeBg(bufferedImage);
            ImageUtils.saveBufferedImage(bufferedImage, "anime_seg.png", "build/output");
        }
    }
}
