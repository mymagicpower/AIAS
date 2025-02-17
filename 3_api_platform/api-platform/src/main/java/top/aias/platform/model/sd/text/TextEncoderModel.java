package top.aias.platform.model.sd.text;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.platform.model.sd.pool.TextEncoderPool;
import top.aias.platform.model.sd.translator.TextEncoder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TextEncoder 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TextEncoderModel implements AutoCloseable {
    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private ZooModel<String, NDList> model;
    private Device device;
    private TextEncoderPool pool;
    private String root;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public TextEncoderModel(String root, int poolSize, Device device) {
        this.root = root;
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
            this.pool = new TextEncoderPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDList predict(String text) throws TranslateException {
        ensureInitialized();
        Predictor<String, NDList> predictor = pool.getPredictor();
        NDList result = predictor.predict(text);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<String, NDList> criteria() {
        Criteria<String, NDList> criteria =
                Criteria.builder()
                        .setTypes(String.class, NDList.class)
                        .optModelPath(Paths.get(root + "text_encoder.pt"))
                        .optEngine("PyTorch")
                        .optProgress(new ProgressBar())
                        .optTranslator(new TextEncoder(root))
                        .optDevice(device)
                        .build();

        return criteria;
    }
}
