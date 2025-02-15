package top.aias.platform.model.det;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
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
 * 人脸检测
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceDetModel implements AutoCloseable {
    private ZooModel<Image, DetectedObjects> model;
    private FaceDetPool faceDetPool;
    private String modelPath;
    private String modelName;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public FaceDetModel(){}

    public FaceDetModel(String modelPath, String modelName, int poolSize, Device device) {
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
            this.faceDetPool = new FaceDetPool(model, poolSize);
            initialized.set(true);
        }
    }

    public DetectedObjects predict(Image img) throws TranslateException {
        ensureInitialized();
        Predictor<Image, DetectedObjects> predictor = faceDetPool.getPredictor();
        DetectedObjects detections = predictor.predict(img);
        faceDetPool.releasePredictor(predictor);
        return detections;
    }

    public void close() {
        if (initialized.get()) {
            this.model.close();
            this.faceDetPool.close();
        }
    }

    private Criteria<Image, DetectedObjects> criteria(String modelPath, String modelName, Device device) {

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new FaceDetTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
