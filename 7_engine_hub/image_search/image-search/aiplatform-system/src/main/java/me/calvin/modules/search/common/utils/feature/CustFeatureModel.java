package me.calvin.modules.search.common.utils.feature;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.mxnet.engine.MxSymbolBlock;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.nn.SymbolBlock;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.*;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 20, 2021
 */
public final class CustFeatureModel {

    private ZooModel<Image, float[]> model;
    private Predictor<Image, float[]> predictor;

    public void init(String newModelPath, String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
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
                        .optModelUrls(modelUri)
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optModelName("resnet50_v2");

        // 加载模型
        Model oldModel = ModelZoo.loadModel(oldBuilder.build());
        SymbolBlock block = (SymbolBlock) oldModel.getBlock();
        // 去掉全连接层
        block.removeLastBlock();

        // 指定模型的搜索目录
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelUrls(newModelPath)
                        .optBlock(block)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optModelName("new_resnet_50")
                        .build();

        this.model = ModelZoo.loadModel(criteria);
        MxSymbolBlock mxSymbolBlock = (MxSymbolBlock) model.getBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();
        mxSymbolBlock.removeLastBlock();

        this.predictor = model.newPredictor();
    }

    public void close() {
        this.model.close();
        this.predictor.close();
    }

    public float[] predict(Image image) throws TranslateException {
        return predictor.predict(image);
    }

    private final class FeatureTranslator implements Translator<Image, float[]> {
        protected Batchifier batchifier = Batchifier.STACK;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
            Pipeline pipeline = new Pipeline();
            pipeline.add(new Resize(224))
                    .add(new ToTensor());
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
