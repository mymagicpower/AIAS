package top.aias.sd.pipelines;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.sd.translator.ImageEncoder;
import top.aias.sd.translator.TextEncoder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * VaeEncoder 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class VaeEncoderModel implements AutoCloseable {
    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private ZooModel<Image, NDArray> model;
    private Device device;
    private VaeEncoderPool pool;
    private String root;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public VaeEncoderModel(String root, int poolSize, Device device) {
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
            this.pool = new VaeEncoderPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDArray predict(Image image) throws TranslateException {
        ensureInitialized();
        Predictor<Image, NDArray> predictor = pool.getPredictor();
        NDArray result = predictor.predict(image);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<Image, NDArray> criteria() {
        Criteria<Image, NDArray> criteria =
                Criteria.builder()
                        .setTypes(Image.class, NDArray.class)
                        .optModelPath(Paths.get(root + "vae_encoder.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new ImageEncoder(HEIGHT,WIDTH))
                        .build();

        return criteria;
    }
}
