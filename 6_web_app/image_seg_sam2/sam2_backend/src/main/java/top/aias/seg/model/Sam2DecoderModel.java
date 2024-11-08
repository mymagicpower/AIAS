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
import top.aias.seg.bean.Sam2Input;
import top.aias.seg.model.pool.DecoderPool;
import top.aias.seg.translator.Sam2DecoderTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class Sam2DecoderModel implements AutoCloseable {
    private ZooModel<Sam2Input, DetectedObjects> model;
    private DecoderPool segPool;

//    public Sam2DecoderModel(String modelUri, int poolSize) throws ModelException, IOException {
//        init(modelUri, poolSize);
//    }

    public void init(String modelUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = criteria(modelUri).loadModel();
        this.segPool = new DecoderPool(model, poolSize);
    }

    public DetectedObjects predict(Sam2Input input) throws TranslateException {
        Predictor<Sam2Input, DetectedObjects> predictor = segPool.getPredictor();
        DetectedObjects detection = predictor.predict(input);
        segPool.releasePredictor(predictor);
        return detection;
    }

    public void close() {
        this.model.close();
        this.segPool.close();
    }

    private Criteria<Sam2Input, DetectedObjects> criteria(String modelUri) {
        Criteria<Sam2Input, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Sam2Input.class, DetectedObjects.class)
//                        .optDevice(device)
                        .optModelPath(Paths.get(modelUri))
                        .optEngine("OnnxRuntime")
                        .optTranslator(new Sam2DecoderTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
