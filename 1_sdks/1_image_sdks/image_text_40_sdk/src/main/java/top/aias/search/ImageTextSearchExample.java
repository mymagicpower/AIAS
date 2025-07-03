package top.aias.search;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.search.utils.FeatureUtils;
import top.aias.search.utils.ImageEncoderModel;
import top.aias.search.utils.TextEncoderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像文本跨模态搜索，支持40种语言
 * Image & Text search【40 Languages】
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class ImageTextSearchExample {

    private static final Logger logger = LoggerFactory.getLogger(ImageTextSearchExample.class);

    private ImageTextSearchExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        List<String> texts = new ArrayList<>();
        texts.add("There are two dogs in the snow.\n");
        texts.add("A cat on the table");
        texts.add("London at night");

        logger.info("texts: {}", Arrays.toString(texts.toArray()));

        Path imageFile = Paths.get("src/test/resources/two_dogs_in_snow.jpg");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        logger.info("image: {}", "src/test/resources/two_dogs_in_snow.jpg");


        try (ImageEncoderModel imageModel = new ImageEncoderModel();
             TextEncoderModel textModel = new TextEncoderModel()) {
            imageModel.init("models/CLIP-ViT-B-32-IMAGE.pt", 4);
            //If text is chinese, isChinese = true, otherwise false
            textModel.init("models/M-BERT-Base-ViT-B/M-BERT-Base-ViT-B.pt", 4, false);

            float[] imageEmbeddings = imageModel.predict(image);
            logger.info("Vector dimension: {}", imageEmbeddings.length);
            logger.info("image embeddings: {}", Arrays.toString(imageEmbeddings));

            List<float[]> list = new ArrayList<>();
            for (String text : texts) {
                float[] textEmbedding = textModel.predict(text);
                list.add(textEmbedding);
            }

            float[] sims = new float[texts.size()];
            for (int i = 0; i < sims.length; i++) {
                logger.info("text [{}] embeddings: {}", texts.get(i), Arrays.toString(list.get(i)));
                sims[i] = 100 * FeatureUtils.cosineSim(imageEmbeddings, list.get(i));
                logger.info("Similarity: {}%", sims[i]);
            }

            logger.info("Label probs: {}", Arrays.toString(FeatureUtils.softmax(sims)));
        }
    }
}
