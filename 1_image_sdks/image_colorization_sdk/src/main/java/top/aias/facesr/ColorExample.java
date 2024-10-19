package top.aias.facesr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.facesr.model.DdcolorModel;
import top.aias.facesr.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 照片上色
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class ColorExample {

    private static final Logger logger = LoggerFactory.getLogger(ColorExample.class);

    private ColorExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/audrey_hepburn.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (DdcolorModel srModel = new DdcolorModel("models/", "traced_ddcolor_cpu.pt", 1, Device.cpu())) {
            Image img = srModel.predict(image);
            ImageUtils.saveImage(img, "ddcolor.png", "build/output");
        }
    }
}
