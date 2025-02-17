package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import top.aias.sd.todo.PipelineLoraTxt2Img;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;

public final class LoraTxt2ImageCpu {

    private LoraTxt2ImageCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        try(PipelineLoraTxt2Img model = new PipelineLoraTxt2Img("H:\\models\\aigc\\sd_cpu\\", Device.cpu());){
            long start = System.currentTimeMillis();
            Image result = model.generateImage("Green pokemon with menacing face", 25);
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);

            ImageUtils.saveImage(result, "lora_txt2img_pt_cpu.png", "build/output");
        }
    }
}