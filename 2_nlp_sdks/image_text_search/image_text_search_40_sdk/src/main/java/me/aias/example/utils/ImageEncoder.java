package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImageEncoder {

  private static final Logger logger = LoggerFactory.getLogger(ImageEncoder.class);

  public ImageEncoder() {}

  public Criteria<Image, float[]> criteria() {

    Criteria<Image, float[]> criteria =
        Criteria.builder()
            .setTypes(Image.class, float[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/clip_series/CLIP-ViT-B-32-IMAGE.zip")
            //            .optModelUrls("/Users/calvin/CLIP-ViT-B-32-IMAGE/")
            .optTranslator(new ImageTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
