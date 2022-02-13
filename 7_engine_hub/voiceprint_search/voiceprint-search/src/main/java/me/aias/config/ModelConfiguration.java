package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.common.voice.VoiceprintModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 模型实例
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Configuration
public class ModelConfiguration {
    // Voiceprint Model
    @Value("${audio.feature}")
    private String feature;

    @Bean
    public VoiceprintModel voiceprintModel() throws IOException, ModelNotFoundException, MalformedModelException {
        VoiceprintModel voiceprintModel = new VoiceprintModel();
        voiceprintModel.init(feature);
        return voiceprintModel;
    }
}