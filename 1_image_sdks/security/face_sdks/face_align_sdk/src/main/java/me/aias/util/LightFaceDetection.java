package me.aias.util;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public final class LightFaceDetection {

  public LightFaceDetection() {}

  public Criteria<Image, DetectedObjects> criteria(int topK, double confThresh, double nmsThresh) {

    double[] variance = {0.1f, 0.2f};

    int[][] scales = {{10, 16, 24}, {32, 48}, {64, 96}, {128, 192, 256}};
    int[] steps = {8, 16, 32, 64};

    FaceDetectionTranslator translator =
        new FaceDetectionTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/face/ultranet.zip")
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .build();

    return criteria;
  }
}
