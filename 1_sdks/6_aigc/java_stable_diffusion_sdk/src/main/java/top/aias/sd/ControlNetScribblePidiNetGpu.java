package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.edge.PidiNetScribbleModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetScribblePidiNetGpu {

    private ControlNetScribblePidiNetGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/pidi_gpu.pt";
        Path imageFile = Paths.get("src/test/resources/scribble.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "royal chamber with fancy bed";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_scribble.pt", Device.gpu());
             PidiNetScribbleModel detector = new PidiNetScribbleModel(512, 512, modelPath,false, 1, Device.gpu())) {
            Image img = detector.predict(image);
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_scribble_pidinet_gpu.png", "build/output");
        }

    }
}