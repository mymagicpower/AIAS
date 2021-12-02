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

// https://www.paddlepaddle.org.cn/hubdetail?name=mobilenet_v2_animals&en_category=ImageClassification
//
// /Users/calvin/Desktop/Download/browser/PaddleHub-release-v2.1/modules/image/classification/mobilenet_v2_animals

public final class LightAnimalsClassification {

  private static final Logger logger = LoggerFactory.getLogger(LightAnimalsClassification.class);

  private LightAnimalsClassification() {}

  public static Classifications predict(Image img)
      throws IOException, ModelException, TranslateException {
    Classifications classifications = LightAnimalsClassification.classfier(img);
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
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/mobilenet_animals.zip")
            // .optModelUrls("/Users/calvin/model/mobilenet_animals/")
            .optModelName("inference")
            .optTranslator(new AnimalTranslator())
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    try (ZooModel rotateModel = ModelZoo.loadModel(criteria)) {
      try (Predictor<Image, Classifications> classifier = rotateModel.newPredictor()) {
        Classifications classifications = classifier.predict(img);
        return classifications;
      }
    }
  }
}
