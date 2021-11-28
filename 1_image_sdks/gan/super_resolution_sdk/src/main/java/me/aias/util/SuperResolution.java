package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class SuperResolution {

  private static final Logger logger = LoggerFactory.getLogger(SuperResolution.class);

  public SuperResolution() {}

  public static void main(String[] args) throws ModelException, TranslateException, IOException {
    String imagePath = "src/test/resources/";
    ImageFactory imageFactory = ImageFactory.getInstance();

    List<Image> inputImages =
        Arrays.asList(imageFactory.fromFile(Paths.get(imagePath + "srgan.png")));

    List<Image> enhancedImages = new SuperResolution().predict(inputImages);

    logger.info("Images generated: {}", enhancedImages.size());
    ImageUtils.saveImages(inputImages, enhancedImages, "build/output/");
  }

  public Image predict(Image img)
      throws IOException, ModelException, TranslateException {
    Criteria<Image, Image> criteria =
        Criteria.builder()
            .setTypes(Image.class, Image.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/esrgan-tf2_1.zip")
            // .optModelUrls("/Users/calvin/Documents/build/tf_models/esrgan-tf2_1")
            .optOption("Tags", "serve")
            .optEngine("TensorFlow") // Use TensorFlow engine
            .optOption("SignatureDefKey", "serving_default")
            .optTranslator(new SuperResolutionTranslator())
            .optProgress(new ProgressBar())
            .build();

    try (ZooModel<Image, Image> model = criteria.loadModel();
        Predictor<Image, Image> enhancer = model.newPredictor()) {
      return enhancer.predict(img);
    }
  }

  public List<Image> predict(List<Image> inputImages)
      throws IOException, ModelException, TranslateException {
    Criteria<Image, Image> criteria =
        Criteria.builder()
            .setTypes(Image.class, Image.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/esrgan-tf2_1.zip")
            // .optModelUrls("/Users/calvin/Documents/build/tf_models/esrgan-tf2_1")
            .optOption("Tags", "serve")
            .optEngine("TensorFlow") // Use TensorFlow engine
            .optOption("SignatureDefKey", "serving_default")
            .optTranslator(new SuperResolutionTranslator())
            .optProgress(new ProgressBar())
            .build();

    try (ZooModel<Image, Image> model = criteria.loadModel();
        Predictor<Image, Image> enhancer = model.newPredictor()) {
      return enhancer.batchPredict(inputImages);
    }
  }
}
