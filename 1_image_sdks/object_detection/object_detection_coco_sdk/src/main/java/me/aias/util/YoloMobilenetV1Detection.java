package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.YoloTranslator;
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

public final class YoloMobilenetV1Detection {

  private static final Logger logger = LoggerFactory.getLogger(YoloMobilenetV1Detection.class);

  public YoloMobilenetV1Detection() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/detection.jpeg");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    double threshold = 0.25;
    DetectedObjects detections = new YoloMobilenetV1Detection().predict(img, threshold);

    logger.info("{}", detections);
  }

  public DetectedObjects predict(Image img, double threshold)
      throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 450);
    arguments.put("height", 450);
    arguments.put("resize", true);
    arguments.put("rescale", true);
    arguments.put("threshold", threshold);
    arguments.put("synsetFileName", "classes.txt");

    Translator<Image, DetectedObjects> translator = YoloTranslator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("MXNet")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/yolo_coco_mobilenetv1.zip")
            // .optModelUrls("/Users/calvin/Documents/build/mxnet_models/yolo_coco_mobilenetv1/")
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
