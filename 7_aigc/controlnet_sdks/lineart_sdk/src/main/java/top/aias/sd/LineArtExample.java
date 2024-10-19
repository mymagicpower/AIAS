package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.controlnet.LineArtDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LineArtExample {

    private static final Logger logger = LoggerFactory.getLogger(LineArtExample.class);

    private LineArtExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/fullbody.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (LineArtDetector detector = new LineArtDetector(512, 512, Device.cpu());) {
            Image depthImg = detector.predict(img);
            ImageUtils.saveImage(depthImg, "lineArt_pt.png", "build/output");
        }
    }
}
