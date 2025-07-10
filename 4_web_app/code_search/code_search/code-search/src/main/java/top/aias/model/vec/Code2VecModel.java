package top.aias.model.vec;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
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
 * 代码编码模型
 *
 * @author calvin
 * @mail 179209347@qq.com
 **/
public final class Code2VecModel implements CodeModel {
    private ZooModel<String, float[]> model;
    private CodeEncoderPool encoderPool;
    private HuggingFaceTokenizer tokenizer;

    public Code2VecModel() {
    }

    public Code2VecModel(String modelPath, String modelName, int poolSize, int maxLength, Device device) throws ModelNotFoundException, MalformedModelException, IOException {
        init(modelPath, modelName, poolSize, maxLength, device);
    }

    public void init(String modelPath, String modelName, int poolSize, int maxLength, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        tokenizer =
                HuggingFaceTokenizer.builder()
                        .optPadding(false)
//                    .optPadToMaxLength()
                        .optMaxLength(maxLength)
                        .optTokenizerPath(Paths.get(modelPath))
                        .optTruncation(true)
//                    .optTokenizerName("moka-ai/m3e-base")
                        .build();

        this.model = ModelZoo.loadModel(ptCriteria(modelPath, modelName, device));
        this.encoderPool = new CodeEncoderPool(model, poolSize);
    }

    public float[] predict(String text) throws TranslateException {
        Predictor<String, float[]> predictor = encoderPool.getPredictor();
        float[] feature = predictor.predict(text);
        encoderPool.releasePredictor(predictor);
        return feature;
    }

    public void close() {
        this.model.close();
        this.encoderPool.close();
        this.tokenizer.close();
    }

    private Criteria<String, float[]> ptCriteria(String modelPath, String modelName, Device device) {
        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optTranslator(new Code2VecTranslator(tokenizer))
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
