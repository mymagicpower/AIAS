package top.aias.seg.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.seg.translator.Sam2EncoderTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * sam2-hiera-large.onnx：sam2大模型。
 * sam2-hiera-tiny.onnx：sam2的轻量级版本。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Sam2EncoderModel implements AutoCloseable {
    private ZooModel<Sam2Input, NDList> model;
    private EncoderPool encoderPool;

    public Sam2EncoderModel(String modelPath, String modelName, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, modelName, poolSize, device);
    }

    public void init(String modelPath, String modelName, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = criteria(modelPath, modelName, device).loadModel();
        this.encoderPool = new EncoderPool(model, poolSize);
    }

    public NDList predict(Sam2Input input) throws TranslateException {
        Predictor<Sam2Input, NDList> predictor = encoderPool.getPredictor();
        NDList detection = predictor.predict(input);
        encoderPool.releasePredictor(predictor);
        return detection;
    }

    public void close() {
        this.model.close();
        this.encoderPool.close();
    }

    private Criteria<Sam2Input, NDList> criteria(String modelPath, String modelName, Device device) {

        Criteria<Sam2Input, NDList> criteria =
                Criteria.builder()
                        .setTypes(Sam2Input.class, NDList.class)
                        .optDevice(device)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optEngine("OnnxRuntime")
                        .optTranslator(new Sam2EncoderTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
