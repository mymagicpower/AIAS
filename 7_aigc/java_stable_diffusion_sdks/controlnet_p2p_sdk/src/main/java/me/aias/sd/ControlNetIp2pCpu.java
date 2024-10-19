package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetIp2pCpu {

    private ControlNetIp2pCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/ip2p.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "make it on fire";

        try(StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_ip2p.pt", Device.cpu());){
            Image result = model.generateImage(image, prompt,"",  25);
            ImageUtils.saveImage(result, "ctrlnet_ip2p_pt_cpu.png", "output");
        }
    }
}