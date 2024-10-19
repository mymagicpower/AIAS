package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.LineArtAnimeDetector;
import me.aias.sd.controlnet.LineArtDetector;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetLineArtAnimeGpu {

    private ControlNetLineArtAnimeGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/lineart.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "A warrior girl in the jungle";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_gpu/", "controlnet_lineart_anime.pt", Device.gpu());
             LineArtAnimeDetector detector = new LineArtAnimeDetector(512, 512, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "lineart_anime.png", "output");
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_lineart_anime_gpu.png", "output");
        }

    }
}