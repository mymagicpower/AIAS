package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.HedDetector;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class HedExample {

    private static final Logger logger = LoggerFactory.getLogger(HedExample.class);

    private HedExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/softedge.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (HedDetector hedEstimation = new HedDetector(512, 512, false, Device.cpu());) {
            Image depthImg = hedEstimation.predict(img);
            ImageUtils.saveImage(depthImg, "hed_pt.png", "build/output");
        }
    }
}
