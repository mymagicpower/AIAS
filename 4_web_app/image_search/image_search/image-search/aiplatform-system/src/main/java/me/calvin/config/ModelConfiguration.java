package me.calvin.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.calvin.modules.search.common.milvus.MilvusEngine;
import me.calvin.modules.search.model.ImageEncoderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Configuration
public class ModelConfiguration {
    @Autowired
    private FileProperties properties;

    // Image Model
    @Value("${model.imageModel}")
    private String imageModel;
    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    @Value("${search.host}")
    private String host;

    @Value("${search.port}")
    private String port;

    @Value("${search.indexType}")
    private String indexType;

    @Value("${search.metricType}")
    private String metricType;

    @Value("${search.dimension}")
    private String dimension;

    @Value("${search.collectionName}")
    private String collectionName;

    @Value("${search.partitionName}")
    private String partitionName;

    @Value("${search.nlist}")
    private String nlist;

    @Bean
    public ImageEncoderModel imageEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        ImageEncoderModel imageEncoderModel = new ImageEncoderModel();
        imageEncoderModel.init(imageModel, poolSize);
        return imageEncoderModel;
    }

//    @Bean
//    public MilvusEngine milvusEngine() {
//        MilvusEngine milvusEngine = new MilvusEngine();
//        milvusEngine.init(host, port, collectionName, partitionName, indexType, metricType, dimension, nlist);
//        return milvusEngine;
//    }
}