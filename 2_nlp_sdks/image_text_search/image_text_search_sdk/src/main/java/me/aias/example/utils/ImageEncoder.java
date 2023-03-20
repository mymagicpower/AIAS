package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ImageEncoder {

  private static final Logger logger = LoggerFactory.getLogger(ImageEncoder.class);

  public ImageEncoder() {}

  public Criteria<Image, float[]> criteria() {

    Criteria<Image, float[]> criteria =
        Criteria.builder()
            .setTypes(Image.class, float[].class)
            .optModelPath(Paths.get("models/CLIP-ViT-B-32-IMAGE.zip"))
            .optTranslator(new ImageTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
