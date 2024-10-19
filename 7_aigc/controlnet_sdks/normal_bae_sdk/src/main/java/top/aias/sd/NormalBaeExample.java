package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.controlnet.NormalBaeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NormalBaeExample {

    private static final Logger logger = LoggerFactory.getLogger(NormalBaeExample.class);

    private NormalBaeExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/toy.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (NormalBaeDetector detector = new NormalBaeDetector(512, 512, Device.cpu());) {
            Image depthImg = detector.predict(img);
            ImageUtils.saveImage(depthImg, "normalbae_pt.png", "build/output");
        }
    }
}
