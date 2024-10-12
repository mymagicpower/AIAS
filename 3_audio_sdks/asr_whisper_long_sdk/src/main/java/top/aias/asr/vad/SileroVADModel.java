package top.aias.asr.vad;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 语音检测模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SileroVADModel implements AutoCloseable {

    ZooModel<NDList, NDList> vadModel;
    Predictor<NDList, NDList> predictor;

    public SileroVADModel() throws ModelNotFoundException, MalformedModelException, IOException {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get("models/silero_vad.onnx"))
                        .optTranslator(new SileroVADTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        vadModel = criteria.loadModel();
        predictor = vadModel.newPredictor();
    }

    public NDList vad(NDList list) throws TranslateException {
        return predictor.predict(list);
    }

    @Override
    public void close() {
        vadModel.close();
        predictor.close();
    }
}
