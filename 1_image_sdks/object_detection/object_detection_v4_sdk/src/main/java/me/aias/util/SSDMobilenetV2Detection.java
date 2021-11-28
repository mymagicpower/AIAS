package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.tensorflow.zoo.cv.objectdetction.TfSsdTranslator;
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

public final class SSDMobilenetV2Detection {

  private static final Logger logger = LoggerFactory.getLogger(SSDMobilenetV2Detection.class);

  public SSDMobilenetV2Detection() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/detection.jpeg");
    Image img = ImageFactory.getInstance().fromFile(imageFile);
    double threshold = 0.4;
    DetectedObjects detections = new SSDMobilenetV2Detection().predict(img, threshold);

    logger.info("{}", detections);
  }

  public DetectedObjects predict(Image img, double threshold)
      throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 256);
    arguments.put("height", 256);
    arguments.put("resize", true);
    arguments.put("threshold", threshold);
    arguments.put("synsetFileName", "classes.txt");

    Translator<Image, DetectedObjects> translator = TfSsdTranslator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
            Criteria.builder()
                    .optEngine("TensorFlow") // Use TensorFlow engine
                    .optOption("Tags", "")
                    .setTypes(Image.class, DetectedObjects.class)
                    .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/tf_mobilenetv2.zip")
//            .optModelUrls("/Users/calvin/Documents/build/tf_models/tf_mobilenet_v2/")
                    .optTranslator(translator)
                    .optProgress(new ProgressBar())
                    .build();

    try (ZooModel<Image, DetectedObjects> ssd = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, DetectedObjects> predictor = ssd.newPredictor()) {
        DetectedObjects detectedObjects = predictor.predict(img);
        return detectedObjects;
      }
    }
  }
}
