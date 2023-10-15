package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.utils.BigGANTranslator;
import me.aias.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An example of generation using BigGAN.
 */
public final class BigGAN {

    private static final Logger logger = LoggerFactory.getLogger(BigGAN.class);

    public BigGAN() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        // size 支持 128, 256, 512
        int size = 512;
        // imageClass 支持imagenet类别1~1000
        long imageClass = 156;

        Criteria<Long, Image> criteria = new BigGAN().generate(size, 0.4f);
        Image image = null;
        try (ZooModel<Long, Image> model = ModelZoo.loadModel(criteria);
             Predictor<Long, Image> generator = model.newPredictor()) {
            image = generator.predict(imageClass);
        }

        ImageUtils.saveImage(image, "image" + imageClass + ".png", "build/output/");
        logger.info("Generated image has been saved in: {}", "build/output/");
    }

    public Criteria<Long, Image> generate(int size, float truncation) {

        Path modelPath = Paths.get("models/biggan128.zip");
        if (size == 128) {
            size = 120;
            modelPath = Paths.get("models/biggan128.zip");
        } else if (size == 256) {
            size = 140;
            modelPath = Paths.get("/models/biggan256.zip");
        } else if (size == 512) {
            size = 128;
            modelPath = Paths.get("models/biggan512.zip");
        }

        BigGANTranslator translator = new BigGANTranslator(size, truncation);
        Criteria<Long, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch") // Use PyTorch engine
                        .setTypes(Long.class, Image.class)
                        .optModelPath(modelPath)
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }
}
