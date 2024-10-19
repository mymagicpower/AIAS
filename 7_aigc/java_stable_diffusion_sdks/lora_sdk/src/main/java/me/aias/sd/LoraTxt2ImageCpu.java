package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import me.aias.sd.pipelines.PipelineLoraTxt2Img;
import me.aias.sd.utils.ImageUtils;

import java.io.IOException;

public final class LoraTxt2ImageCpu {

    private LoraTxt2ImageCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        try(PipelineLoraTxt2Img model = new PipelineLoraTxt2Img("models/pytorch_cpu/", Device.cpu());){
            long start = System.currentTimeMillis();
            Image result = model.generateImage("Green pokemon with menacing face", 25);
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);

            ImageUtils.saveImage(result, "lora_txt2img_pt_cpu.png", "output");
        }
    }
}