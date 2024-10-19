package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.controlnet.PidiNetScribbleDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://huggingface.co/lllyasviel/control_v11p_sd15_softedge
public final class PidiNetScribbleCPUExample {

    private static final Logger logger = LoggerFactory.getLogger(PidiNetScribbleCPUExample.class);

    private PidiNetScribbleCPUExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/bag.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (PidiNetScribbleDetector detector = new PidiNetScribbleDetector(512, 512,false, Device.cpu());) {
            Image depthImg = detector.predict(img);
            ImageUtils.saveImage(depthImg, "pidiNetScribble_pt_cpu.png", "build/output");
        }
    }
}
