package top.aias.seg;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.seg.model.Sam2Model;
import top.aias.seg.translator.Sam2Translator;
import top.aias.seg.utils.OpenCVUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Sam2图像分割
 * 提供2个模型，只支持CPU
 * sam2-hiera-large.pt
 * sam2-hiera-tiny.pt
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Sam2Example {

    private static final Logger logger = LoggerFactory.getLogger(Sam2Example.class);

    private Sam2Example() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/truck.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        Sam2Translator.Sam2Input input =
                Sam2Translator.Sam2Input.builder(image).addPoint(575, 750).addBox(425, 600, 700, 875).build();

        try (Sam2Model sam2Model = new Sam2Model("models/", "sam2-hiera-tiny.pt", 1)) {
            DetectedObjects detection = sam2Model.predict(input);
            showMask(input, detection);
            logger.info("{}", detection);
        }
    }

    private static void showMask(Sam2Translator.Sam2Input input, DetectedObjects detection) throws IOException {
        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        Image img = input.getImage();
        img.drawBoundingBoxes(detection, 0.8f);
        img.drawMarks(input.getPoints());
        for (Rectangle rect : input.getBoxes()) {
            OpenCVUtils.drawRectangle((Mat)img.getWrappedImage(),rect, 0xff0000, 6);
        }

        Path imagePath = outputDir.resolve("sam2.png");
        img.save(Files.newOutputStream(imagePath), "png");
    }
}
