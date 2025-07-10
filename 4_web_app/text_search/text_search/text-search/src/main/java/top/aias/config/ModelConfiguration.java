package top.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.common.model.TextEncoderModel;
import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Configuration
public class ModelConfiguration {
    // Text Model
    @Value("${model.modelPath}")
    private String modelPath;
    @Value("${model.modelName}")
    private String modelName;
    @Value("${model.chinese}")
    private boolean chinese;

    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    @Bean
    public TextEncoderModel textEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TextEncoderModel textEncoderModel = new TextEncoderModel();
        textEncoderModel.init(modelPath, modelName, poolSize, chinese);
        return textEncoderModel;
    }
}