package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.seg.SegUperNetModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetSegGpu {

    private ControlNetSegGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/upernet.pt";
        Path imageFile = Paths.get("src/test/resources/seg.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "old house in stormy weather with rain and wind";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_seg.pt", Device.gpu());
             SegUperNetModel detector = new SegUperNetModel(512, 512, modelPath, false, 1, Device.gpu())) {
            Image img = detector.predict(image);
            ImageUtils.saveImage(img, "segUperNet.png", "build/output");
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_seg_gpu.png", "build/output");
        }

    }
}