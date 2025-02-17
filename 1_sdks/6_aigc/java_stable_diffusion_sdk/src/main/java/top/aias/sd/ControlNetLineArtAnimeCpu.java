package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.lineart.LineArtAnimeModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetLineArtAnimeCpu {

    private ControlNetLineArtAnimeCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "models/lineart_anime.pt";
        Path imageFile = Paths.get("src/test/resources/animeroom.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "royal chamber with fancy bed";

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_cpu\\", "controlnet_lineart_anime.pt", Device.cpu());
             LineArtAnimeModel detector = new LineArtAnimeModel(512, 512, modelPath, 1, Device.cpu())) {
            Image img = detector.predict(image);
            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_lineart_anime_cpu.png", "build/output");
        }

    }
}