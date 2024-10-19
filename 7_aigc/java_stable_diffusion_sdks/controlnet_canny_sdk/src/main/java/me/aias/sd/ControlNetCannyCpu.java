package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;
import me.aias.sd.utils.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetCannyCpu {

    private ControlNetCannyCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/input_image_vermeer.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        // Canny
        org.opencv.core.Mat mat = OpenCVUtils.canny((org.opencv.core.Mat) image.getWrappedImage());
        image = OpenCVImageFactory.getInstance().fromImage(mat);

        String prompt = "masterpiece, best quality, ultra detailed, extremely detailed CG unity 8k wallpaper, best illumination, best shadow, an extremely delicate and beautiful, dynamic angle, finely detail, depth of field, bloom, shine, glinting stars, classic, illustration, painting, highres, original, perfect lighting";
        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_canny.pt", Device.cpu());) {
            Image result = model.generateImage(image, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_canny_pt_cpu.png", "output");
        }
    }
}