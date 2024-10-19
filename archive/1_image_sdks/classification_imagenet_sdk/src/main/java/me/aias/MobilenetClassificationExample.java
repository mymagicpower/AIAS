package me.aias;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.util.MobilenetClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class MobilenetClassificationExample {

  private static final Logger logger = LoggerFactory.getLogger(MobilenetClassificationExample.class);

  private MobilenetClassificationExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/cup.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    Classifications classifications = new MobilenetClassification().predict(image);
    
    Classifications.Classification bestItem = classifications.best();
    System.out.println(bestItem.getClassName() + " : " + bestItem.getProbability());
    //    List<Classifications.Classification> items = classifications.items();
    //    List<String> names = new ArrayList<>();
    //    List<Double> probs = new ArrayList<>();
    //    for (int i = 0; i < items.size(); i++) {
    //      Classifications.Classification item = items.get(i);
    //      names.add(item.getClassName());
    //      probs.add(item.getProbability());
    //    }
    
    logger.info("{}", classifications);
  }
}
