package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.controlnet.SegUperNetDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SegUperNetExample {

    private static final Logger logger = LoggerFactory.getLogger(SegUperNetExample.class);

    private SegUperNetExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/aaa.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SegUperNetDetector detector = new SegUperNetDetector(512, 512, false, Device.cpu());) {
            Image segImg = detector.predict(img);
            ImageUtils.saveImage(segImg, "segUperNet.png", "build/output");
        }
    }
}
