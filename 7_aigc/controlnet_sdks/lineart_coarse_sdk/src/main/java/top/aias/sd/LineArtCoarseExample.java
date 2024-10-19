package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.controlnet.LineArtCoarseDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LineArtCoarseExample {

    private static final Logger logger = LoggerFactory.getLogger(LineArtCoarseExample.class);

    private LineArtCoarseExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/lineart.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (LineArtCoarseDetector detector = new LineArtCoarseDetector(512, 512, Device.cpu());) {
            Image depthImg = detector.predict(img);
            ImageUtils.saveImage(depthImg, "lineArtCoarse_pt.png", "build/output");
        }
    }
}
