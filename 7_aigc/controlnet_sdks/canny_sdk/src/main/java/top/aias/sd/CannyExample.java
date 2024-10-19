package top.aias.sd;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.utils.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CannyExample {

    private CannyExample() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/bird.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        // Canny
        org.opencv.core.Mat mat = OpenCVUtils.canny((org.opencv.core.Mat) image.getWrappedImage());
        image = OpenCVImageFactory.getInstance().fromImage(mat);
        ImageUtils.saveImage(image, "canny.png", "build/output");
    }
}