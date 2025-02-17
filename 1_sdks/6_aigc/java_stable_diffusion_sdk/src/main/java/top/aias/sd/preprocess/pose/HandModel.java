package top.aias.sd.preprocess.pose;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * OpenPose 姿态检测,手部关键点模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class HandModel implements AutoCloseable {
    private ZooModel<NDArray, NDArray> model;
    private Device device;
    private FaceHandPool pool;
    private String modelPath;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public HandModel(String modelPath, int poolSize, Device device) {
        this.modelPath = modelPath;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.pool = new FaceHandPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDArray predict(NDArray ndArray) throws TranslateException {
        ensureInitialized();
        Predictor<NDArray, NDArray> predictor = pool.getPredictor();
        NDArray result = predictor.predict(ndArray);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<NDArray, NDArray> criteria() {

        Criteria<NDArray, NDArray> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(NDArray.class, NDArray.class)
                        .optModelPath(Paths.get(modelPath)) // hand.pt
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
