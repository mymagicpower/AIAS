package top.aias.sd.pipelines;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * UNet 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class UNetForControlModel implements AutoCloseable {
    private ZooModel<NDList, NDList> model;
    private Device device;
    private UNetPool pool;
    private String root;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public UNetForControlModel(String root, int poolSize, Device device) {
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
            this.pool = new UNetPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDList predict(NDList ndList) throws TranslateException {
        ensureInitialized();
        Predictor<NDList, NDList> predictor = pool.getPredictor();
        NDList result = predictor.predict(ndList);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<NDList, NDList> criteria() {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(root + "controlnet_unet.pt"))
                        .optEngine("PyTorch")
                        .optProgress(new ProgressBar())
                        .optTranslator(new NoopTranslator())
                        .optDevice(device)
                        .build();

        return criteria;
    }
}
