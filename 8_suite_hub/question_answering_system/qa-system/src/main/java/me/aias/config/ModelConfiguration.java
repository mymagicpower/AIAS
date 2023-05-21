package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.common.sentence.SentenceEncoderModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 模型配置
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Configuration
public class ModelConfiguration {
    @Value("${nlp.feature}")
    private String feature;

    @Bean
    public SentenceEncoderModel sentenceEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        SentenceEncoderModel sentenceEncoderModel = new SentenceEncoderModel();
        sentenceEncoderModel.init(feature);
        return sentenceEncoderModel;
    }
}