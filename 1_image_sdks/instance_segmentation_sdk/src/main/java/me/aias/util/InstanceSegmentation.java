package me.aias.util;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.InstanceSegmentationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import me.aias.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InstanceSegmentation {

  private static final Logger logger = LoggerFactory.getLogger(InstanceSegmentation.class);

  public InstanceSegmentation() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/detection.jpeg");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    DetectedObjects detection = new InstanceSegmentation().predict(img);

    ImageUtils.drawBoundingBoxImage(img, detection);

    logger.info("{}", detection);
  }

  public DetectedObjects predict(Image img) throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("normalize", true);
    arguments.put("synsetFileName", "classes.txt");

    Translator<Image, DetectedObjects> translator =
        InstanceSegmentationTranslator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("MXNet") // Use MxNet engine
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/mask_rcnn_resnet18_v1b_coco.zip"))
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .build();

    try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
        DetectedObjects detection = predictor.predict(img);
        return detection;
      }
    }
  }
}
