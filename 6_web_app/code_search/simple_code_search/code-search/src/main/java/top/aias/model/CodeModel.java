package top.aias.model;

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
import top.aias.model.code2vec.Code2VecTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 代码编码模型接口
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface CodeModel extends AutoCloseable {
    void init(String modelPath, String modelName, int poolSize, int maxLength) throws MalformedModelException, ModelNotFoundException, IOException;
    float[] predict(String text) throws TranslateException;
}
