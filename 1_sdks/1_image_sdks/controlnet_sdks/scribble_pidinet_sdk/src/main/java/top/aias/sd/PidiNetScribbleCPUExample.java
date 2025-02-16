package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.PidiNetScribbleModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Scribble 涂鸦 PidiNet 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
// https://huggingface.co/lllyasviel/control_v11p_sd15_softedge
public final class PidiNetScribbleCPUExample {

    private static final Logger logger = LoggerFactory.getLogger(PidiNetScribbleCPUExample.class);

    private PidiNetScribbleCPUExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/bag.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/pidi_cpu.pt";

        try (PidiNetScribbleModel model = new PidiNetScribbleModel(512, 512, modelPath,false, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "pidiNetScribble_pt_cpu.png", "build/output");
        }
    }
}
