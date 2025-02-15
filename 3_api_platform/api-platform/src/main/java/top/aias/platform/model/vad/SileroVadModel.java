package top.aias.platform.model.vad;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import top.aias.platform.model.sr.SrPool;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

    public SileroVadModel(){}

    public SileroVadModel(String modelPath, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, poolSize, device);
    }

    public void init(String modelPath, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelPath, device));
        this.vadPool = new VadPool(model, poolSize);
    }

    public NDList predict(NDList list) throws TranslateException {
        Predictor<NDList, NDList> predictor = vadPool.getPredictor();
        NDList predict = predictor.predict(list);
        vadPool.releasePredictor(predictor);
        return predict;
    }


    public Criteria<NDList, NDList> criteria(String modelPath, Device device) {
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

    public void close(){
        model.close();
    }
}