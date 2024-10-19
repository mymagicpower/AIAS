package top.aias.sd;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.utils.ShuffleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ContentShuffleExample {

    private static final Logger logger = LoggerFactory.getLogger(ContentShuffleExample.class);

    private ContentShuffleExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/input.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (NDManager manager = NDManager.newBaseManager()) {
            Image newImage = ShuffleUtils.hwcContentShuffle(manager, img, 512, 512);
//            img = OpenCVImageFactory.getInstance().fromNDArray(ndArray);

            ImageUtils.saveImage(newImage, "contentShuffle.png", "build/output");
        }
    }
}
