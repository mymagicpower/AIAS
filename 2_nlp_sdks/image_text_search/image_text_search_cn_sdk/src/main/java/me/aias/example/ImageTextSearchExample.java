package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.ImageEncoder;
import me.aias.Jieba;
import me.aias.TextEncoder;
import me.aias.translation.Translation;
import me.aias.util.FeatureComparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ImageTextSearchExample {

  private static final Logger logger = LoggerFactory.getLogger(ImageTextSearchExample.class);

  private ImageTextSearchExample() {}

  /**
   *  Image & Text search [Chinese]
   *
   * @author calvin
   * @mail 179209347@qq.com
   * @website www.aias.top
   */
  public static void main(String[] args) throws IOException, ModelException, TranslateException {

    List<String> texts = new ArrayList<>();
    texts.add("在雪地里有两条狗");
    texts.add("一只猫在桌子上");
    texts.add("夜晚的伦敦");

    logger.info("texts: {}", Arrays.toString(texts.toArray()));

    Path imageFile = Paths.get("src/test/resources/two_dogs_in_snow.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    logger.info("image: {}", "src/test/resources/two_dogs_in_snow.jpg");

    System.setProperty("ai.djl.default_engine", "PaddlePaddle");
    // Tokenizer for chinese
    Jieba jieba = new Jieba();
    // Translation : chinese to english
    Translation senta = new Translation();
    // Text encoder
    TextEncoder sentenceEncoder = new TextEncoder();
    // Image encoder
    ImageEncoder imageEncoder = new ImageEncoder();
    Criteria<String[], String[]> SentaCriteria = senta.criteria();
    try (ZooModel<String[], String[]> sentaModel = SentaCriteria.loadModel();
         Predictor<String[], String[]> sentaPredictor = sentaModel.newPredictor();
         ZooModel<String, float[]> textModel = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> textPredictor = textModel.newPredictor();
         ZooModel<Image, float[]> imageModel = ModelZoo.loadModel(imageEncoder.criteria());
         Predictor<Image, float[]> imagePredictor = imageModel.newPredictor()) {

      float[] imageEmbeddings = imagePredictor.predict(image);
      logger.info("Vector dimension: {}", imageEmbeddings.length);
      logger.info("image embeddings: {}", Arrays.toString(imageEmbeddings));



      List<String> newTexts = new ArrayList<>();
      for (String text : texts) {
        // Tokenizer
        String[] lacResult = jieba.cut(text);
        logger.info("Tokens : " + Arrays.toString(lacResult));
        // Translation result
        String[] translationResult = sentaPredictor.predict(lacResult);
        logger.info("{} translated to: {}",text, Arrays.toString(translationResult));
        newTexts.add(translationResult[0]);
      }

      // single text
      // float[] textEmbeddings = textPredictor.predict(text);
      // batch texts
      List<float[]> list = textPredictor.batchPredict(newTexts);

      float[] sims = new float[texts.size()];
      for (int i = 0; i < sims.length; i++) {
        logger.info("text [{}] embeddings: {}", texts.get(i), Arrays.toString(list.get(i)));
        sims[i] = 100 * FeatureComparison.cosineSim(imageEmbeddings, list.get(i));
        logger.info("Similarity: {}%", sims[i]);
      }

      logger.info("Label probs: {}", Arrays.toString(FeatureComparison.softmax(sims)));
    }
  }
}
