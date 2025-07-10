package top.aias.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 文本编码模型
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class TextEncoderModel implements AutoCloseable {
    private ZooModel<String, float[]> model;
    private TextEncoderPool encoderPool;

    public void init(String modelUri, int poolSize, boolean isChinese) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(ptCriteria(modelUri, isChinese));
        this.encoderPool = new TextEncoderPool(model, poolSize);
    }

    public ZooModel<String, float[]> getModel() {
        return model;
    }

    public float[] predict(String text) throws TranslateException {
        Predictor<String, float[]> predictor = encoderPool.getPredictor();
        float[] feature = predictor.predict(text);
        encoderPool.releasePredictor(predictor);
        return feature;
    }

    public void close(){
        this.model.close();
        this.encoderPool.close();
    }

    private Criteria<String, float[]> ptCriteria(String modelUri, boolean isChinese) {

        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new TextTranslator(isChinese))
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
