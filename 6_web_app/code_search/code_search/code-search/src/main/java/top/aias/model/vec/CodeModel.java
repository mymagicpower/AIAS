package top.aias.model.vec;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;

import java.io.IOException;

/**
 * 代码编码模型接口
 *
 * @author calvin
 * @mail 179209347@qq.com
 **/
public interface CodeModel extends AutoCloseable {
    void init(String modelPath, String modelName, int poolSize, int maxLength, Device device) throws MalformedModelException, ModelNotFoundException, IOException;
    float[] predict(String text) throws TranslateException;
}
