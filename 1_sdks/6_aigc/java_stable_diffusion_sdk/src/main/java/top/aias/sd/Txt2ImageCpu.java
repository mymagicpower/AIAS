package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionTxt2ImgPipeline;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;

public final class Txt2ImageCpu {

    private Txt2ImageCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        try(StableDiffusionTxt2ImgPipeline model = new StableDiffusionTxt2ImgPipeline("H:\\models\\aigc\\sd_cpu\\", Device.cpu());){
            long start = System.currentTimeMillis();

            Image result = model.generateImage("Photograph of an astronaut riding a horse in desert", 25);

            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);

            ImageUtils.saveImage(result, "txt2img_pt_cpu.png", "build/output");
        }
    }
}