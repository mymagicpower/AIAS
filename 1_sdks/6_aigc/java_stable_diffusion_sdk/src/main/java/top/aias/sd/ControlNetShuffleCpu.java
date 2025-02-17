package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.utils.ShuffleUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetShuffleCpu {

    private ControlNetShuffleCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/shuffle.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        String prompt = "New York";
        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_cpu\\", "controlnet_shuffle.pt", Device.cpu());
             NDManager manager = NDManager.newBaseManager(Device.cpu());) {
            Image newImage = ShuffleUtils.hwcContentShuffle(manager, image, 512, 512);

            long start = System.currentTimeMillis();

            Image result = model.generateImage(newImage, prompt, "", 25);

            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);


            ImageUtils.saveImage(result, "ctrlnet_shuffle_gpu.png", "build/output");
        }
    }
}