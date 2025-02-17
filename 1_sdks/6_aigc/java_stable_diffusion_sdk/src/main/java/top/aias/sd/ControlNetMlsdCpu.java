package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.lineart.MlsdModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetMlsdCpu {

    private ControlNetMlsdCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/mlsd.pt";
        Path imageFile = Paths.get("src/test/resources/mlsd.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "royal chamber with fancy bed";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_cpu\\", "controlnet_mlsd.pt", Device.cpu());
             MlsdModel detector = new MlsdModel(512, 512, modelPath, 1, Device.cpu())) {

            Image img = detector.predict(image);

            long start = System.currentTimeMillis();

            Image result = model.generateImage(img, prompt, "", 25);

            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start)/1000f/60f);

            ImageUtils.saveImage(result, "ctrlnet_mlsd_cpu.png", "build/output");
        }

    }
}