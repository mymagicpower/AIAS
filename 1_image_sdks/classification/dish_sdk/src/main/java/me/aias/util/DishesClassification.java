package me.aias.util;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// https://www.paddlepaddle.org.cn/hubdetail?name=resnet50_vd_dishes&en_category=ImageClassification
public final class DishesClassification {

  private static final Logger logger = LoggerFactory.getLogger(DishesClassification.class);

  private DishesClassification() {}

  public static Classifications predict(Image img)
      throws IOException, ModelException, TranslateException {
    Classifications classifications = DishesClassification.classfier(img);
    List<Classifications.Classification> items = classifications.items();
    double sum = 0;
    double max = 0;
    double[] probArr = new double[items.size()];

    List<String> names = new ArrayList<>();
    List<Double> probs = new ArrayList<>();

    for (int i = 0; i < items.size(); i++) {
      Classifications.Classification item = items.get(i);
      double prob = item.getProbability();
      probArr[i] = prob;
      if (prob > max) max = prob;
    }

    for (int i = 0; i < items.size(); i++) {
      probArr[i] = Math.exp(probArr[i] - max);
      sum = sum + probArr[i];
    }

    for (int i = 0; i < items.size(); i++) {
      Classifications.Classification item = items.get(i);
      names.add(item.getClassName());
      probs.add(probArr[i]);
    }

    return new Classifications(names, probs);
  }

  public static Classifications classfier(Image img)
      throws IOException, ModelException, TranslateException {

    Criteria<Image, Classifications> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, Classifications.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/dishes.zip")
            // .optModelUrls("/Users/calvin/model/dishes/")
            .optModelName("inference")
            .optTranslator(new DishTranslator())
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    try (ZooModel model = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, Classifications> classifier = model.newPredictor()) {
        Classifications classifications = classifier.predict(img);
        return classifications;
      }
    }
  }
}
