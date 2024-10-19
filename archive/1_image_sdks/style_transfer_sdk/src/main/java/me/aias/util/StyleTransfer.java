package me.aias.util;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

public final class StyleTransfer {

  public StyleTransfer() {}

  public enum Artist {
    CEZANNE, // 塞尚(Paul Cezanne, 1838～1906)
    MONET, // 莫奈 (Claude monet, 1840～1926)
    UKIYOE, // 日本浮世绘
    VANGOGH // 梵高 (Vincent Willem van Gogh, 1853~1890)
  }

  public Criteria<Image, Image> criteria(Artist artist) {

    String modelName = "style_" + artist.toString().toLowerCase() + ".zip";
    String modelPath = "models/" + modelName;

    Criteria<Image, Image> criteria =
        Criteria.builder()
            .setTypes(Image.class, Image.class)
            .optEngine("PyTorch") // Use PyTorch engine
             .optModelPath(Paths.get(modelPath))
            .optProgress(new ProgressBar())
            .optDevice(Device.cpu())
            .optTranslatorFactory(new StyleTransferTranslatorFactory())
            .build();

    return criteria;
  }
}
