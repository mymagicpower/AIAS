package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.lineart.LineArtCoarseModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetLineArtCoarseGpu {

    private ControlNetLineArtCoarseGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/lineart_coarse.pt";
        Path imageFile = Paths.get("src/test/resources/girl.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "raw photo, 1girl1 beautiful, detailed clear eves, light red lips, pink hair, pink dress, collarbone. highly detailed, sharp focus";
        String negativePrompt = "defommed,distorted";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_lineart.pt", Device.gpu());
             LineArtCoarseModel detector = new LineArtCoarseModel(512, 512, modelPath, 1, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "lineart.png", "build/output");
            Image result = model.generateImage(img, prompt, negativePrompt, 25);
            ImageUtils.saveImage(result, "ctrlnet_lineart_coarse_gpu.png", "build/output");
        }

    }
}