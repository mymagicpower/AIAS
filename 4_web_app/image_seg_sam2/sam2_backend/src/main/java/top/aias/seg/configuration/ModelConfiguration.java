package top.aias.seg.configuration;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.seg.model.Sam2DecoderModel;
import top.aias.seg.model.Sam2EncoderModel;

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
    @Value("${model.sam2.encoder}")
    private String encoder;

    @Value("${model.sam2.decoder}")
    private String decoder;
    @Value("${model.poolSize}")
    private int poolSize;

    @Bean
    public Sam2EncoderModel sam2EncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        Sam2EncoderModel sam2EncoderModel = new Sam2EncoderModel();
        sam2EncoderModel.init(encoder, poolSize);
        return sam2EncoderModel;
    }

    @Bean
    public Sam2DecoderModel sam2DecoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        Sam2DecoderModel sam2DecoderModel = new Sam2DecoderModel();
        sam2DecoderModel.init(decoder, poolSize);
        return sam2DecoderModel;
    }
}