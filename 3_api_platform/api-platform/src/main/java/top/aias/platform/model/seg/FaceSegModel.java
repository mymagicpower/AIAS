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
import top.aias.platform.model.seg.pool.FaceSegPool;
import top.aias.platform.model.seg.translator.FaceSegTranslator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 人脸分割
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceSegModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private FaceSegPool faceSegPool;
    private String modelPath;
    private String modelName;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);


    public FaceSegModel(){}

    public FaceSegModel(String modelPath, String modelName, int poolSize, Device device) {
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
            this.faceSegPool = new FaceSegPool(model, poolSize);
            initialized.set(true);
        }
    }

    public Image predict(Image img) throws TranslateException {
        ensureInitialized();
        Predictor<Image, Image> predictor = faceSegPool.getPredictor();
        Image segImg = predictor.predict(img);
        faceSegPool.releasePredictor(predictor);
        return segImg;
    }

    public void close() {
        if (initialized.get()) {
            this.model.close();
            this.faceSegPool.close();
        }
    }

    private Criteria<Image, Image> criteria(String modelPath, String modelName, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new FaceSegTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
