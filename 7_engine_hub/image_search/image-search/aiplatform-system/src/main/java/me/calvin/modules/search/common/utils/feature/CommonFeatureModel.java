package me.calvin.modules.search.common.utils.feature;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.mxnet.engine.MxSymbolBlock;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
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
public final class CommonFeatureModel {

    private ZooModel<Image, float[][]> model;
    private Predictor<Image, float[][]> predictor;

    public void init(String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelUri));
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

    public float[][] predict(Image image) throws TranslateException {
        return predictor.predict(image);
    }

    private Criteria<Image, float[][]> criteria(String modelUri) {
        Criteria<Image, float[][]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[][].class)
                        .optModelUrls(modelUri)
                        .optTranslator(new CommonFeatureTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("MXNet") // Use MXNet engine
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }


    private final class CommonFeatureTranslator implements Translator<Image, float[][]> {
        protected Batchifier batchifier = Batchifier.STACK;

        CommonFeatureTranslator() {
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

        public float[][] processOutput(TranslatorContext ctx, NDList list) {
            NDList result = new NDList();
            long numOutputs = list.singletonOrThrow().getShape().get(0);
            for (int i = 0; i < numOutputs; i++) {
                result.add(list.singletonOrThrow().get(i));
            }
            return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }
    }
}
