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
import java.util.Arrays;

/**
 * 图像文本跨模态搜索，支持40种语言
 * Image & Text search【40 Languages】
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class ImageTextSearchExample2 {

    private static final Logger logger = LoggerFactory.getLogger(ImageTextSearchExample2.class);

    private ImageTextSearchExample2() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {

        String text = "There are two dogs in the snow.";

        Path imageFile = Paths.get("src/test/resources/two_dogs_in_snow.jpg");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        Path imageFile2 = Paths.get("src/test/resources/2_7.jpeg");
        Image image2 = ImageFactory.getInstance().fromFile(imageFile2);

        try (ImageEncoderModel imageModel = new ImageEncoderModel();
             TextEncoderModel textModel = new TextEncoderModel()) {
            imageModel.init("models/CLIP-ViT-B-32-IMAGE.pt", 4);
            //If text is chinese, isChinese = true, otherwise false
            textModel.init("models/M-BERT-Base-ViT-B/M-BERT-Base-ViT-B.pt", 4, false);

            float[] feature = imageModel.predict(image);
            float[] feature2 = imageModel.predict(image2);

            logger.info("Vector dimension: {}", feature.length);
            logger.info("image embeddings: {}", Arrays.toString(feature));
            logger.info("image embeddings2: {}", Arrays.toString(feature2));

            float[] textFeature = textModel.predict(text);
            float sims;
            sims = 100 * FeatureUtils.cosineSim(feature, textFeature);
            logger.info("Similarity: {}%", sims);
            sims = 100 * FeatureUtils.cosineSim(feature2, textFeature);
            logger.info("Similarity: {}%", sims);

        }
    }
}
