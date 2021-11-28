package me.aias.infer;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.mxnet.engine.MxSymbolBlock;
import ai.djl.nn.SymbolBlock;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
public final class ImageClassification {

    private static final Logger logger = LoggerFactory.getLogger(ImageClassification.class);
    public static int imgSize = 224;

    private ImageClassification() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String newModelPath = "/Users/calvin/Documents/build/training/modelv2/";
        String imageUrl = "https://car2.autoimg.cn/cardfs/product/g26/M0A/5D/33/1024x0_1_q95_autohomecar__ChsEe15-6YKAULPgAAYVp-IEjZA085.jpg";
        Image img = ImageFactory.getInstance().fromUrl(imageUrl);

        Classifications classifications = predict(newModelPath, img);
        logger.info("Predict result: {}", classifications.topK(3));
    }

    public static Classifications predict(String newModelPath, Image img)
            throws IOException, ModelException, TranslateException {
        ImageClassificationTranslator oldTranslator =
                ImageClassificationTranslator.builder()
                        .addTransform(new ToTensor())
                        .optSynsetArtifactName("synset.txt")
                        .optApplySoftmax(true)
                        .build();

        Criteria.Builder<Image, Classifications> oldBuilder =
                Criteria.builder()
                        .setTypes(Image.class, Classifications.class)
                        .optTranslator(oldTranslator)
                        .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/resnet50_v2.zip")
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optModelName("resnet50_v2");

        // 加载模型
        Model oldModel = ModelZoo.loadModel(oldBuilder.build());
        SymbolBlock block = (SymbolBlock) oldModel.getBlock();
        // 去掉全连接层
        block.removeLastBlock();


        ImageClassificationTranslator translator =
                ImageClassificationTranslator.builder()
                        .addTransform(new ToTensor())
                        .optSynsetArtifactName("synset.txt")
                        .optApplySoftmax(true)
                        .build();

        // 指定模型的搜索目录
        Criteria.Builder<Image, Classifications> builder =
                Criteria.builder()
                        .setTypes(Image.class, Classifications.class)
                        .optModelUrls(newModelPath)
                        .optBlock(block)
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optModelName("new_resnet_50");


        ZooModel<Image, Classifications> modelWithParams = ModelZoo.loadModel(builder.build());
        MxSymbolBlock mxSymbolBlock = (MxSymbolBlock) modelWithParams.getBlock();
        //去掉后面几层，直到512维输出层
//        mxSymbolBlock.removeLastBlock();
//        mxSymbolBlock.removeLastBlock();
//        mxSymbolBlock.removeLastBlock();
//        mxSymbolBlock.removeLastBlock();
//        mxSymbolBlock.removeLastBlock();
//        mxSymbolBlock.removeLastBlock();

        Predictor<Image, Classifications> predictor = modelWithParams.newPredictor();
        return predictor.predict(img);
    }
}
