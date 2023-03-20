package me.aias.example.utils.layout;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LayoutDetection {

  private static final Logger logger = LoggerFactory.getLogger(LayoutDetection.class);

  public LayoutDetection() {}

  public Criteria<Image, DetectedObjects> criteria() {

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/ppyolov2_r50vd_dcn_365e_publaynet_infer.zip"))
            .optTranslator(new LayoutDetectionTranslator())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
