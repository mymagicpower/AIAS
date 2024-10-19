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

public final class ControlNetInpaintCpu {

    private ControlNetInpaintCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/dog.png");
        Image original_image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        imageFile = Paths.get("src/test/resources/dog_mask.png");
        Image mask_image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        String prompt = "Face of a yellow cat, high resolution, sitting on a park bench";
        String negative_prompt = "lowres, bad anatomy, cropped, worst quality";

        try(StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_inpaint.pt", Device.cpu());){
            Image result = model.generateImage(original_image,mask_image, prompt, negative_prompt, 25);
            ImageUtils.saveImage(result, "ctrlnet_inpaint_pt_cpu.png", "output");
        }
    }
}