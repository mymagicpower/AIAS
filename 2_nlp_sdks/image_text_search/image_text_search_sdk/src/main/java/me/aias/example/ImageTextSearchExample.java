package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.FeatureComparison;
import me.aias.example.utils.ImageEncoder;
import me.aias.example.utils.TextEncoder;
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
   * Image & Text search【English】
   *
   * @author calvin
   * @mail 179209347@qq.com
   * @website www.aias.top
   */
  public static void main(String[] args) throws IOException, ModelException, TranslateException {

    List<String> texts = new ArrayList<>();
    texts.add("a diagram");
    texts.add("a dog");
    texts.add("a cat");

    logger.info("texts: {}", Arrays.toString(texts.toArray()));

    Path imageFile = Paths.get("src/test/resources/CLIP.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    logger.info("image: {}", "src/test/resources/CLIP.png");

    TextEncoder sentenceEncoder = new TextEncoder();
    ImageEncoder imageEncoder = new ImageEncoder();

    try (ZooModel<String, float[]> textModel = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> textPredictor = textModel.newPredictor();
         ZooModel<Image, float[]> imageModel = ModelZoo.loadModel(imageEncoder.criteria());
         Predictor<Image, float[]> imagePredictor = imageModel.newPredictor()) {

      float[] imageEmbeddings = imagePredictor.predict(image);
      logger.info("Vector dimension: {}", imageEmbeddings.length);
      logger.info("image embeddings: {}", Arrays.toString(imageEmbeddings));

      // single text
      // float[] textEmbeddings = textPredictor.predict(text);
      // batch texts
      List<float[]> list = textPredictor.batchPredict(texts);
      for (float[] item : list) {}

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
