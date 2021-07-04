package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import me.calvin.animal.AnimalsClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AnimalsClassificationExample {

  private static final Logger logger = LoggerFactory.getLogger(AnimalsClassificationExample.class);

  private AnimalsClassificationExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/tiger.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Classifications classifications = AnimalsClassification.predict(image);

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
