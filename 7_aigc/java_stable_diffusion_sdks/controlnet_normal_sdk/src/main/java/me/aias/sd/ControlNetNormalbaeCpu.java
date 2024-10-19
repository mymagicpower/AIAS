package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.NormalBaeDetector;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetNormalbaeCpu {

    private ControlNetNormalbaeCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/normalbae.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "A head full of roses";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_normalbae.pt", Device.cpu());
             NormalBaeDetector detector = new NormalBaeDetector(512, 512, Device.cpu())) {
            Image img = detector.predict(image);

            long start = System.currentTimeMillis();

            Image result = model.generateImage(img, prompt, "", 25);

            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);

            ImageUtils.saveImage(result, "ctrlnet_normalbae_cpu.png", "output");
        }

    }
}