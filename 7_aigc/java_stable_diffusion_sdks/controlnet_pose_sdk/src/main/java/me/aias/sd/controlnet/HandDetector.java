package me.aias.sd.controlnet;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Paths;

public final class HandDetector implements AutoCloseable {
    private Device device;
    ZooModel model;
    Predictor<NDArray, NDArray> predictor;
    public HandDetector(Device device) throws ModelException, IOException {
        this.device = device;
        this.model = ModelZoo.loadModel(criteria());
        this.predictor = model.newPredictor();
    }

    public NDArray predict(NDArray input) throws TranslateException {
        return predictor.predict(input);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<NDArray, NDArray> criteria() {

        Criteria<NDArray, NDArray> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(NDArray.class, NDArray.class)
                        .optModelPath(Paths.get("models/pytorch/hand.pt"))
                        .optDevice(device)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private final class FeatureTranslator implements Translator<NDArray, NDArray> {
        protected Batchifier batchifier = Batchifier.STACK;
        private int width;
        private int height;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, NDArray array) {
            NDManager manager = ctx.getNDManager();
            return new NDList(array);
        }

        @Override
        public NDArray processOutput(TranslatorContext ctx, NDList list) {
            NDArray array = list.singletonOrThrow();
            array.detach();
            return array;
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }

    }
}
