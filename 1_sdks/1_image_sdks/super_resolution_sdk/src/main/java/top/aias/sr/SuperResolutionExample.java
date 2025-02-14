package top.aias.sr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sr.model.SrModel;
import top.aias.sr.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 超分辨- 提升 4 倍分辨率
 * Super Resolution - Enhance Resolution
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class SuperResolutionExample {

    private static final Logger logger = LoggerFactory.getLogger(SuperResolutionExample.class);

    private SuperResolutionExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/srgan.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SrModel srModel = new SrModel("models/", "realsr_traced_model.pt", 1, Device.cpu())) {
            // 如果宽或高大于 1080，先缩小，再高清放大
            // 否则，直接放大4倍
            // 可以调高，最大支持多少，自己试验【取决于显存，内存，算法本身】。
            if(image.getHeight()> 1080 || image.getHeight() > 1080){
                image = image.resize(image.getWidth() / 4, image.getHeight() / 4, true);
                Image img = srModel.predict(image);
                ImageUtils.saveImage(img, "Real-ESRGAN.png", "build/output");
            }else {
                Image img = srModel.predict(image);
                ImageUtils.saveImage(img, "Real-ESRGAN.png", "build/output");
            }
        }
    }
}
