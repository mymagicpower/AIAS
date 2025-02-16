package top.aias.platform.model.preprocess.pose;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import top.aias.platform.utils.PoseUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * OpenPose 姿态检测,人脸关键点模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceModel implements AutoCloseable {
    private ZooModel<NDArray, NDArray> model;
    private Device device;
    private FaceHandPool pool;
    private String modelPath;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public FaceModel(String modelPath, int poolSize, Device device) {
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
                        .optModelPath(Paths.get(modelPath)) // face.pt
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
