package top.aias.sd.controlnet;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import top.aias.sd.utils.PoseUtils;

import java.io.IOException;
import java.nio.file.Paths;

public final class FaceDetector implements AutoCloseable {
    private Device device;
    ZooModel model;
    Predictor<NDArray, NDArray> predictor;
    public FaceDetector( Device device) throws ModelException, IOException {
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
                        .optModelPath(Paths.get("models/face.pt"))
                        .optDevice(device)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private final class FeatureTranslator implements Translator<NDArray, NDArray> {
        protected Batchifier batchifier = Batchifier.STACK;
        private int w_size = 384;
        private float thre = 0.05f; // 0.05f

        private int width;
        private int height;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, NDArray array) {
            NDManager manager = ctx.getNDManager();
            NDArray x_data = PoseUtils.smart_resize(array, w_size, w_size);
            x_data = x_data.toType(DataType.FLOAT32, false);
            x_data = x_data.div(256.0f).sub(0.5f);
            x_data = x_data.transpose(2, 0, 1);

            x_data = x_data.flip(0);

            return new NDList(x_data);
        }

        @Override
        public NDArray processOutput(TranslatorContext ctx, NDList list) {
            NDArray array = list.get(list.size() - 1);


            array.detach();
            return array;
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }

    }
}
