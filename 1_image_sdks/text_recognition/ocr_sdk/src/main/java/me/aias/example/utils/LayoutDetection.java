package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LayoutDetection {

  private static final Logger logger = LoggerFactory.getLogger(LayoutDetection.class);

  public LayoutDetection() {}

  public Criteria<Image, DetectedObjects> criteria() {

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ppyolov2_r50vd_dcn_365e_publaynet_infer.zip")
            //            .optModelUrls(
            // "/Users/calvin/.paddledet/inference_model/ppyolov2_r50vd_dcn_365e_publaynet/ppyolov2_r50vd_dcn_365e_publaynet_infer")
            // .optDevice(Device.cpu())
            .optTranslator(new LayoutDetectionTranslator())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
