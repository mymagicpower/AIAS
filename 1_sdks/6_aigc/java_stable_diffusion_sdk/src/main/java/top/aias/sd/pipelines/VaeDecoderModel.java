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
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import top.aias.sd.translator.ImageDecoder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * VaeDecoder 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class VaeDecoderModel implements AutoCloseable {
    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private ZooModel<NDArray, Image> model;
    private Device device;
    private VaeDecoderPool pool;
    private String root;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public VaeDecoderModel(String root, int poolSize, Device device) {
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
            this.pool = new VaeDecoderPool(model, poolSize);
            initialized.set(true);
        }
    }

    public Image predict(NDArray ndArray) throws TranslateException {
        ensureInitialized();
        Predictor<NDArray, Image> predictor = pool.getPredictor();
        Image result = predictor.predict(ndArray);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<NDArray, Image> criteria() {
        Criteria<NDArray, Image> criteria =
                Criteria.builder()
                        .setTypes(NDArray.class, Image.class)
                        .optModelPath(Paths.get(root + "vae_decoder.pt"))
                        .optEngine("PyTorch")
                        .optTranslator(new ImageDecoder(HEIGHT, WIDTH))
                        .optProgress(new ProgressBar())
                        .optDevice(device)
                        .build();

        return criteria;
    }
}
