package me.aias.util;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Joints;
import ai.djl.modality.cv.translator.SimplePoseTranslator;
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

public final class PoseResnet18Estimation {

  private static final Logger logger = LoggerFactory.getLogger(PersonDetection.class);

  public PoseResnet18Estimation() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/pedestrian.png");
    Image img = ImageFactory.getInstance().fromFile(imageFile);

    Joints detections = new PoseResnet18Estimation().predict(img);

    logger.info("{}", detections);
  }

  public Joints predict(Image img) throws IOException, ModelException, TranslateException {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 192);
    arguments.put("height", 256);
    arguments.put("resize", true);
    arguments.put("normalize", true);
    arguments.put("threshold", 0.2);

    Translator<Image, Joints> translator = SimplePoseTranslator.builder(arguments).build();

    Criteria<Image, Joints> criteria =
        Criteria.builder()
            .optEngine("MXNet")
            .setTypes(Image.class, Joints.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/simple_pose_resnet18_v1b.zip")
            // .optModelUrls("/Users/calvin/Documents/build/mxnet_models/simple_pose_resnet18_v1b/")
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    try (ZooModel<Image, Joints> pose = ModelZoo.loadModel(criteria);
        Predictor<Image, Joints> predictor = pose.newPredictor()) {
      Joints joints = predictor.predict(img);
      return joints;
    }
  }
}
