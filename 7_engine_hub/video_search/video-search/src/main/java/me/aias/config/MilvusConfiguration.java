package me.aias.config;

import me.aias.common.milvus.MilvusConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 图片操作常量类
 * @author Calvin
 * @date 2021-12-12
 **/
@Configuration
public class MilvusConfiguration {
    @Value("${search.host}")
    private String host;
    @Value("${search.port}")
    private int port;
    
    @Bean
    public MilvusConnector milvusConnector() {
        MilvusConnector milvus = new MilvusConnector();
        milvus.init(host, port);
        return milvus;
    }
}