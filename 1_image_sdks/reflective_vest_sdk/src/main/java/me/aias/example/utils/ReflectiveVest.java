package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://github.com/gengyanlei/reflective-clothes-detect-yolov5/blob/master/README_ZN.md
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/
public final class ReflectiveVest {

  private static final Logger logger = LoggerFactory.getLogger(ReflectiveVest.class);

  public ReflectiveVest() {}

  public Criteria<Image, DetectedObjects> criteria() {
    Map<String, Object> arguments = new ConcurrentHashMap<>();
    arguments.put("width", 640);
    arguments.put("height", 640);
    arguments.put("resize", true);
    arguments.put("rescale", true);
    //    arguments.put("toTensor", false);
    //    arguments.put("range", "0,1");
    //    arguments.put("normalize", "false");
    arguments.put("threshold", 0.2);
    arguments.put("nmsThreshold", 0.5);

    Translator<Image, DetectedObjects> translator = YoloV5Translator.builder(arguments).build();

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/reflective_clothes.zip"))
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .optEngine("PyTorch")
            .optDevice(Device.cpu())
            .build();

    return criteria;
  }
}
