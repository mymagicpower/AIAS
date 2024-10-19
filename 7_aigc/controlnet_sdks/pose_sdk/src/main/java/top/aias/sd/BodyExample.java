package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.BodyDetector;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class BodyExample {

    private static final Logger logger = LoggerFactory.getLogger(BodyExample.class);

    private BodyExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/pose.png"); // pose.png beauty.jpg
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (BodyDetector body = new BodyDetector(512, 512, Device.cpu())) {

            Image depthImg = body.predict(img);
            ImageUtils.saveImage(depthImg, "body.png", "build/output");
        }
    }
}
