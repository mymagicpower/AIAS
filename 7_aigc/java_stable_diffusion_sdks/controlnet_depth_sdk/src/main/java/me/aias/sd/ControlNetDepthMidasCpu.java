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

public final class ControlNetDepthMidasCpu {

    private ControlNetDepthMidasCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/depth.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "Stormtrooper's lecture in beautiful lecture hall";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_depth.pt", Device.cpu());
             MidasDepthDetector detector = new MidasDepthDetector(512, 512, Device.cpu())) {
            Image depthImg = detector.predict(image);
            Image result = model.generateImage(depthImg, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_midas_depth_pt_cpu.png", "output");
        }

    }
}