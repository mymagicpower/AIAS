package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.MidasDepthDetector;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetDepthMidasGpu {

    private ControlNetDepthMidasGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/depth.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "Stormtrooper's lecture in beautiful lecture hall";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_gpu/", "controlnet_depth.pt", Device.gpu());
             MidasDepthDetector detector = new MidasDepthDetector(512, 512, Device.gpu())) {
            Image depthImg = detector.predict(image);
            ImageUtils.saveImage(depthImg, "depthImg.png", "output");
            Image result = model.generateImage(depthImg, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_midas_depth_pt_gpu.png", "output");
        }

    }
}