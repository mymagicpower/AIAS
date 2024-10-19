package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.SingleShotDetectionTranslator;
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

public final class SSDResnet50Detection {

  private static final Logger logger = LoggerFactory.getLogger(SSDResnet50Detection.class);

  public SSDResnet50Detection() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/pedestrian.png");
    Image img = ImageFactory.getInstance().fromFile(imageFile);
    double threshold = 0.5;
    DetectedObjects detections = new SSDResnet50Detection().predict(img, threshold);

    logger.info("{}", detections);
  }

  public DetectedObjects predict(Image img, double threshold)
      throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 512);
    arguments.put("height", 512);
    arguments.put("resize", true);
    arguments.put("rescale", true);
    arguments.put("threshold", threshold);

    Translator<Image, DetectedObjects> translator =
        SingleShotDetectionTranslator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("MXNet")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/ssd_512_resnet50_v1_voc.zip"))
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
