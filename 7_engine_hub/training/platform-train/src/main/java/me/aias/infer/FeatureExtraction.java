package me.aias.infer;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.mxnet.engine.MxSymbolBlock;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.nn.SymbolBlock;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
public final class FeatureExtraction {

    private static final Logger logger = LoggerFactory.getLogger(FeatureExtraction.class);
    public static int imgSize = 224;

    private FeatureExtraction() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String newModelPath = "/Users/calvin/Documents/build/training/modelv2/";
        Image img = ImageFactory.getInstance().fromUrl("/Users/calvin/Documents/Data_Faces_0/5.jpg");

        float[] feature = predict(newModelPath, img);
        System.out.println(feature.length);
        if (feature != null) {
            logger.info(Arrays.toString(feature));
        }

    }

    public static float[] predict(String newModelPath, Image img)
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

        // 指定模型的搜索目录
        Criteria.Builder<Image, float[]> builder =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelUrls(newModelPath)
                        .optBlock(block)
                        .optTranslator(new FeatureExtraction.FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optModelName("new_resnet_50");


        ZooModel<Image, float[]> modelWithParams = ModelZoo.loadModel(builder.build());
        MxSymbolBlock mxSymbolBlock = (MxSymbolBlock) modelWithParams.getBlock();
        //去掉后面几层，直到512维输出层
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();

        Predictor<Image, float[]> predictor = modelWithParams.newPredictor();
        return predictor.predict(img);
    }

    private static final class FeatureTranslator implements Translator<Image, float[]> {
        protected Batchifier batchifier = Batchifier.STACK;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
            Pipeline pipeline = new Pipeline();
            pipeline.add(new Resize(imgSize))
                    .add(new ToTensor());
//                    .add(new Normalize(Cifar10.NORMALIZE_MEAN, Cifar10.NORMALIZE_STD));
            NDList list = pipeline.transform(new NDList(array));
            return list;
        }

        public float[] processOutput(TranslatorContext ctx, NDList list) {
            NDList result = new NDList();
            long numOutputs = list.singletonOrThrow().getShape().get(0);
            for (int i = 0; i < numOutputs; i++) {
                result.add(list.singletonOrThrow().get(i));
            }
            float[][] embeddings = result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
            float[] feature = new float[embeddings.length];
            for (int i = 0; i < embeddings.length; i++) {
                feature[i] = embeddings[i][0];
            }
            return feature;
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }
    }
}
