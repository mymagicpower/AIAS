package top.aias.asr.configuration;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.asr.model.WhisperModel;

import java.io.IOException;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Configuration
public class ModelConfiguration {
    @Value("${model.type}")
    private String type;

    @Value("${model.uri.tiny}")
    private String tinyModel;

    @Value("${model.uri.base}")
    private String baseModel;

    @Value("${model.uri.small}")
    private String smallModel;

    @Value("${model.poolSize}")
    private int poolSize;

    @Bean
    public WhisperModel whisperModel() throws IOException, ModelNotFoundException, MalformedModelException {
        WhisperModel whisperModel = new WhisperModel();

        int kvLength = 0;
        int encoderIndex = 0;

        switch (type) {
            case "tiny":
                kvLength = 16;
                encoderIndex = 22;
                whisperModel.init(tinyModel, poolSize, kvLength, encoderIndex);
                break;
            case "base":
                kvLength = 24;
                encoderIndex = 32;
                whisperModel.init(baseModel, poolSize, kvLength, encoderIndex);
                break;
            case "small":
                kvLength = 48;
                encoderIndex = 62;
                whisperModel.init(smallModel, poolSize, kvLength, encoderIndex);
                break;
        }

        return whisperModel;
    }

}