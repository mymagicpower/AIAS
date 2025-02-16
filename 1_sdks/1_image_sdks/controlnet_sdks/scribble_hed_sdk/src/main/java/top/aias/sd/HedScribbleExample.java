package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.HedScribbleModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Scribble 涂鸦 Hed 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class HedScribbleExample {

    private static final Logger logger = LoggerFactory.getLogger(HedScribbleExample.class);

    private HedScribbleExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/toy.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/hed.pt";

        try (HedScribbleModel model = new HedScribbleModel(512, 512, modelPath, true, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "hedScribble.png", "build/output");
        }
    }
}
