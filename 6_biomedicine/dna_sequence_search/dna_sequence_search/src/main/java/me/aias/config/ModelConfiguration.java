package me.aias.config;

import me.aias.common.sentence.VectorizerModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模型配置
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Configuration
public class ModelConfiguration {
    // 设定词汇表的最大量为768
    @Value("${search.size}")
    private int size;

    @Bean
    public VectorizerModel sentenceEncoderModel() {
        VectorizerModel vectorizerModel = new VectorizerModel();
        vectorizerModel.init(size);
        return vectorizerModel;
    }
}