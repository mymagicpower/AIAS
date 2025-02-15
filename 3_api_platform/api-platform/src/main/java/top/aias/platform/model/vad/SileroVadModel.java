package top.aias.platform.model.vad;

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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 静音检测
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class SileroVadModel implements AutoCloseable{

    private ZooModel<NDList, NDList> model;
    private VadPool vadPool;
    private String modelPath;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public SileroVadModel(){}

    public SileroVadModel(String modelPath, int poolSize, Device device){
        this.modelPath = modelPath;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria(modelPath, device));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.vadPool = new VadPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDList predict(NDList list) throws TranslateException {
        ensureInitialized();
        Predictor<NDList, NDList> predictor = vadPool.getPredictor();
        NDList predict = predictor.predict(list);
        vadPool.releasePredictor(predictor);
        return predict;
    }

    private Criteria<NDList, NDList> criteria(String modelPath, Device device) {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(modelPath))
                        .optTranslator(new SileroVADTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .optDevice(device)
                        .build();
        return criteria;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            vadPool.close();
        }
    }
}