package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.util.ImageUtils;
import me.aias.util.StyleTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 风格迁移
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */

public final class StyleTransferExample {

  private static final Logger logger = LoggerFactory.getLogger(StyleTransferExample.class);

  private StyleTransferExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String imagePath = "src/test/resources/scenery.jpeg";
    Image input = ImageFactory.getInstance().fromFile(Paths.get(imagePath));
    StyleTransfer styleTransfer = new StyleTransfer();

    // 梵高风格 (Vincent Willem van Gogh, 1853~1890)
    StyleTransfer.Artist artist = StyleTransfer.Artist.VANGOGH;
    try (ZooModel<Image, Image> model = ModelZoo.loadModel(styleTransfer.criteria(artist));
         Predictor<Image, Image> predictor = model.newPredictor()) {
      Image output = predictor.predict(input);
      ImageUtils.saveImage(output, artist.toString().toLowerCase()+ ".png", "build/output/");
    }

    // 塞尚风格 (Paul Cezanne, 1838～1906)
    artist = StyleTransfer.Artist.CEZANNE;

    try (ZooModel<Image, Image> model = ModelZoo.loadModel(styleTransfer.criteria(artist));
         Predictor<Image, Image> predictor = model.newPredictor()) {
      Image output = predictor.predict(input);
      ImageUtils.saveImage(output, artist.toString().toLowerCase()+ ".png", "build/output/");
    }

    // 莫奈风格 (Claude monet, 1840～1926)
    artist = StyleTransfer.Artist.MONET;
    try (ZooModel<Image, Image> model = ModelZoo.loadModel(styleTransfer.criteria(artist));
         Predictor<Image, Image> predictor = model.newPredictor()) {
      Image output = predictor.predict(input);
      ImageUtils.saveImage(output, artist.toString().toLowerCase()+ ".png", "build/output/");
    }

    // 日本浮世绘风格
    artist = StyleTransfer.Artist.UKIYOE;
    try (ZooModel<Image, Image> model = ModelZoo.loadModel(styleTransfer.criteria(artist));
         Predictor<Image, Image> predictor = model.newPredictor()) {
      Image output = predictor.predict(input);
      ImageUtils.saveImage(output, artist.toString().toLowerCase()+ ".png", "build/output/");
    }
    
    logger.info("Images generated and saved in folder - build/output/");
  }
}
