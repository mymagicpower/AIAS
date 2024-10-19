package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.LineArtDetector;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetLineArtGpu {

    private ControlNetLineArtGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/lineart.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "michael jackson concert";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_gpu/", "controlnet_lineart.pt", Device.gpu());
             LineArtDetector detector = new LineArtDetector(512, 512, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "lineart.png", "output");
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_lineart_gpu.png", "output");
        }

    }
}