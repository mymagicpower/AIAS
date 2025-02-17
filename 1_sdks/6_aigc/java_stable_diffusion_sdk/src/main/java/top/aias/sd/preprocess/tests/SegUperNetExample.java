package top.aias.sd.preprocess.tests;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.preprocess.seg.SegUperNetModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Segmentation 语义分割
 * 语义分割可多通道应用，原理是用颜色把不同类型的对象分割开，让AI能正确识别对象类型和需求生成的区界。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SegUperNetExample {

    private static final Logger logger = LoggerFactory.getLogger(SegUperNetExample.class);

    private SegUperNetExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/aaa.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "models/upernet.pt";

        try (SegUperNetModel model = new SegUperNetModel(512, 512, modelPath, false, 1, Device.cpu())) {
            Image segImg = model.predict(img);
            ImageUtils.saveImage(segImg, "segUperNet.png", "build/output");
        }
    }
}
