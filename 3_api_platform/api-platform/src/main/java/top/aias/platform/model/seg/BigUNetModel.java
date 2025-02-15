package top.aias.platform.model.seg;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.platform.model.seg.pool.SegPool;
import top.aias.platform.model.seg.translator.UNetTranslator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * u2net：通用的的预训练模型，通常用这个就行。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class BigUNetModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private SegPool segPool;
    private String modelPath;
    private String modelName;
    private int poolSize;
    private boolean mask;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);


    public BigUNetModel(){}

    public BigUNetModel(String modelPath, String modelName, int poolSize, boolean mask, Device device) {
        this.modelPath = modelPath;
        this.modelName = modelName;
        this.poolSize = poolSize;
        this.mask = mask;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria(modelPath, modelName, mask, device));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.segPool = new SegPool(model, poolSize);
            initialized.set(true);
        }
    }

    public Image predict(Image img) throws TranslateException {
        ensureInitialized();
        Predictor<Image, Image> predictor = segPool.getPredictor();
        Image segImg = predictor.predict(img);
        segPool.releasePredictor(predictor);
        return segImg;
    }

    public void close() {
        if (initialized.get()) {
            this.model.close();
            this.segPool.close();
        }
    }

    private Criteria<Image, Image> criteria(String modelPath, String modelName, boolean mask, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new UNetTranslator(mask))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
