package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.DptDepthDetector;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DptDepthEstimationExample {

    private static final Logger logger = LoggerFactory.getLogger(DptDepthEstimationExample.class);

    private DptDepthEstimationExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/stormtrooper.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (DptDepthDetector dptDepth = new DptDepthDetector(512, 512, Device.cpu())) {
            Image depthImg = dptDepth.predict(img);
            ImageUtils.saveImage(depthImg, "dptDepth.png", "build/output");
        }
    }
}
