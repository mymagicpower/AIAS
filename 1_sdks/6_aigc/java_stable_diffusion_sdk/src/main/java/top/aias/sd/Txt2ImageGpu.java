package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionTxt2ImgPipeline;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;

public final class Txt2ImageGpu {

    private Txt2ImageGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String prompt = "Photograph of an astronaut riding a horse in desert";

        try(StableDiffusionTxt2ImgPipeline model = new StableDiffusionTxt2ImgPipeline("H:\\models\\aigc\\sd_gpu\\", Device.gpu());){
            Image result = model.generateImage(prompt, "",25);
            ImageUtils.saveImage(result, "txt2img_pt_gpu.png", "build/output");
        }
    }
}