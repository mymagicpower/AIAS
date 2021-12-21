package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.model.ImageEncoderModel;
import me.aias.model.TextEncoderModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 模型实例
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Configuration
public class ModelConfiguration {
    // Text Model
    @Value("${model.textModel}")
    private String textModel;
    @Value("${model.chinese}")
    private boolean chinese;

    // Image Model
    @Value("${model.imageModel}")
    private String imageModel;

    @Bean
    public TextEncoderModel textEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TextEncoderModel textEncoderModel = new TextEncoderModel();
        textEncoderModel.init(textModel, chinese);
        return textEncoderModel;
    }

    @Bean
    public ImageEncoderModel imageEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        ImageEncoderModel imageEncoderModel = new ImageEncoderModel();
        imageEncoderModel.init(imageModel);
        return imageEncoderModel;
    }
}