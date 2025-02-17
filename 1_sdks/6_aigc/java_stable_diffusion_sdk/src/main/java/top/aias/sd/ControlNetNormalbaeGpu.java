package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.normal.NormalBaeModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetNormalbaeGpu {

    private ControlNetNormalbaeGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/normalbae.pt";
        Path imageFile = Paths.get("src/test/resources/normalbae.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "A man made of roses";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_normalbae.pt", Device.gpu());
             NormalBaeModel detector = new NormalBaeModel(512, 512, modelPath, 1, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "normalbae.png", "build/output");
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_normalbae_gpu.png", "build/output");
        }

    }
}