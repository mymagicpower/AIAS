package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MobilenetClassification {

  private static final Logger logger = LoggerFactory.getLogger(MobilenetClassification.class);

  public MobilenetClassification() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/cup.jpeg");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    Classifications classification = new MobilenetClassification().predict(img);

    logger.info("{}", classification);
  }

  public Classifications predict(Image img) throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 224);
    arguments.put("height", 224);
    arguments.put("centerCrop", true);
    arguments.put("resize", true);
    arguments.put("applySoftmax", true);

    Translator<Image, Classifications> translator = ImageClassificationTranslator.builder(arguments).build();

    Criteria<Image, Classifications> criteria =
        Criteria.builder()
            .optEngine("MXNet")
            .setTypes(Image.class, Classifications.class)
             .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/mobilenetv3_small.zip")
//            .optModelUrls("/Users/calvin/Documents/build/mxnet_models/mobilenetv3_small/")
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .build();

    try (ZooModel<Image, Classifications> pose = ModelZoo.loadModel(criteria);
         Predictor<Image, Classifications> predictor = pose.newPredictor()) {
      Classifications classification = predictor.predict(img);
      return classification;
    }
  }
}
