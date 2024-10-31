package top.aias.seg.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.seg.translator.Sam2Translator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * sam2-hiera-large.pt：sam2大模型。
 * sam2-hiera-tiny.pt：sam2的轻量级版本。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Sam2Model implements AutoCloseable {
    private ZooModel<Sam2Translator.Sam2Input, DetectedObjects> model;
    private SegPool segPool;

    public Sam2Model(String modelPath, String modelName, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, modelName, poolSize, device);
    }

    public void init(String modelPath, String modelName, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = criteria(modelPath, modelName, device).loadModel();
        this.segPool = new SegPool(model, poolSize);
    }

    public DetectedObjects predict(Sam2Translator.Sam2Input input) throws TranslateException {
        Predictor<Sam2Translator.Sam2Input, DetectedObjects> predictor = segPool.getPredictor();
        DetectedObjects detection = predictor.predict(input);
        segPool.releasePredictor(predictor);
        return detection;
    }

    public void close() {
        this.model.close();
        this.segPool.close();
    }

    private Criteria<Sam2Translator.Sam2Input, DetectedObjects> criteria(String modelPath, String modelName, Device device) {
        String encode = null;
        if(!device.isGpu())
            encode = "encode";

        Criteria<Sam2Translator.Sam2Input, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Sam2Translator.Sam2Input.class, DetectedObjects.class)
                        // sam2-hiera-tiny
                        // sam2-hiera-large
                        .optDevice(device)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optEngine("PyTorch")
//                        .optOption("mapLocation","true")
                        .optTranslator(new Sam2Translator(encode))
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
