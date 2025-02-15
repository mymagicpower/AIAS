package top.aias.platform.model.sr;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 超分辨- 提升分辨率
 * Super Resolution - Enhance Resolution
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class SrModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private SrPool srPool;
    private String modelPath;
    private String modelName;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public SrModel(){}

    public SrModel(String modelPath, String modelName, int poolSize, Device device) {
        this.modelPath = modelPath;
        this.modelName = modelName;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria(modelPath, modelName, device));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.srPool = new SrPool(model, poolSize);
            initialized.set(true);
        }
    }

    public Image predict(Image img) throws TranslateException, ModelException, IOException {
        ensureInitialized();
        Predictor<Image, Image> predictor = srPool.getPredictor();
        Image segImg = predictor.predict(img);
        srPool.releasePredictor(predictor);
        return segImg;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            srPool.close();
        }
    }

    private Criteria<Image, Image> criteria(String modelPath, String modelName, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new SrTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
