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
import java.util.Arrays;

public final class ImageTextSearchExample2 {

    private static final Logger logger = LoggerFactory.getLogger(ImageTextSearchExample2.class);

    private ImageTextSearchExample2() {
    }

    /**
     * Image & Text search【40 Languages】
     *
     * @author calvin
     * @mail 179209347@qq.com
     * @website www.aias.top
     */
    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        String text = "在雪地里有两条狗";

        Path imageFile = Paths.get("src/test/resources/two_dogs_in_snow.jpg");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        Path imageFile2 = Paths.get("src/test/resources/2_7.jpeg");
        Image image2 = ImageFactory.getInstance().fromFile(imageFile2);

        TextEncoder sentenceEncoder = new TextEncoder();
        ImageEncoder imageEncoder = new ImageEncoder();

        //If text is chinese, isChinese = true, otherwise isChinese = false
        try (ZooModel<String, float[]> textModel = ModelZoo.loadModel(sentenceEncoder.criteria(true));
             Predictor<String, float[]> textPredictor = textModel.newPredictor();
             ZooModel<Image, float[]> imageModel = ModelZoo.loadModel(imageEncoder.criteria());
             Predictor<Image, float[]> imagePredictor = imageModel.newPredictor()) {

            float[] imageEmbeddings = imagePredictor.predict(image);
            float[] imageEmbeddings2 = imagePredictor.predict(image2);
            logger.info("Vector dimension: {}", imageEmbeddings.length);
            logger.info("image embeddings: {}", Arrays.toString(imageEmbeddings));
            logger.info("image embeddings2: {}", Arrays.toString(imageEmbeddings2));
            
            float[] textEmbedding = textPredictor.predict(text);
            float sims;
            sims = 100 * FeatureComparison.cosineSim(imageEmbeddings, textEmbedding);
            logger.info("Similarity: {}%", sims);
            sims = 100 * FeatureComparison.cosineSim(imageEmbeddings2, textEmbedding);
            logger.info("Similarity: {}%", sims);
            
        }
    }
}
