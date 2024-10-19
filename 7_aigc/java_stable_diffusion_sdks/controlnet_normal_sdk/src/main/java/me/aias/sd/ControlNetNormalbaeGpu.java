package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.LineArtAnimeDetector;
import me.aias.sd.controlnet.NormalBaeDetector;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetNormalbaeGpu {

    private ControlNetNormalbaeGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/normalbae.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "A head full of roses";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_gpu/", "controlnet_normalbae.pt", Device.gpu());
             NormalBaeDetector detector = new NormalBaeDetector(512, 512, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "normalbae.png", "output");
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_normalbae_gpu.png", "output");
        }

    }
}