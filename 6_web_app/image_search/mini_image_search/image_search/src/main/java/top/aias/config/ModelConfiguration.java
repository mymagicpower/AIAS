package top.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.model.ImageEncoderModel;

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
    // Image Model
    @Value("${model.imageModel}")
    private String imageModel;
    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    @Bean
    public ImageEncoderModel imageEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        ImageEncoderModel imageEncoderModel = new ImageEncoderModel();
        imageEncoderModel.init(imageModel, poolSize);
        return imageEncoderModel;
    }
}