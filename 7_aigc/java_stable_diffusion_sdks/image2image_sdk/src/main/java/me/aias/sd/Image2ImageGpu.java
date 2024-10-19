package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.pipelines.StableDiffusionImg2ImgPipeline;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Image2ImageGpu {

    private Image2ImageGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/sketch-mountains-input.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "A fantasy landscape, trending on artstation";

        try(StableDiffusionImg2ImgPipeline model = new StableDiffusionImg2ImgPipeline("models/pytorch_gpu/", Device.gpu());){
            Image result = model.generateImage(image, prompt,"",  25);
            ImageUtils.saveImage(result, "img2img_pt_gpu.png", "output");
        }
    }
}