package me.aias.util;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PersonDetection {

  private static final Logger logger = LoggerFactory.getLogger(PersonDetection.class);

  public PersonDetection() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/pedestrian.png");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    DetectedObjects detections = new PersonDetection().predict(img);

    logger.info("{}", detections);
  }

  public DetectedObjects predict(Image img) throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 512);
    arguments.put("height", 512);
    arguments.put("resize", true);
    arguments.put("rescale", true);
    arguments.put("threshold", 0.2);

    Translator<Image, DetectedObjects> translator =
        SingleShotDetectionTranslator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("MXNet")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/ssd_512_resnet50_v1_voc.zip")
            // .optModelUrls("/Users/calvin/Documents/build/mxnet_models/ssd_512_resnet50_v1_voc/")
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    try (ZooModel<Image, DetectedObjects> ssd = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, DetectedObjects> predictor = ssd.newPredictor()) {
        DetectedObjects detectedObjects = predictor.predict(img);
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        List<DetectedObjects.DetectedObject> items = detectedObjects.items();
        for (DetectedObjects.DetectedObject item : items) {
          if ("person".equals(item.getClassName())) {
            names.add(item.getClassName());
            prob.add(item.getProbability());
            rect.add(item.getBoundingBox());
          }
        }
        return new DetectedObjects(names, prob, rect);
      }
    }
  }
}
