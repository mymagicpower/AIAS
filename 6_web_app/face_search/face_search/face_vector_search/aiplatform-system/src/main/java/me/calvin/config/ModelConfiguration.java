package me.calvin.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.calvin.modules.search.common.milvus.MilvusEngine;
import me.calvin.modules.search.face.FaceDetectionModel;
import me.calvin.modules.search.face.FaceFeatureModel;
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
    //Face Model
    @Value("${face.det}")
    private String faceDet;
    @Value("${face.feature}")
    private String feature;
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
    public FaceDetectionModel faceDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceDetectionModel faceDetectionModel = new FaceDetectionModel();
        faceDetectionModel.init(faceDet, poolSize);
        return faceDetectionModel;
    }

    @Bean
    public FaceFeatureModel faceFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceFeatureModel faceFeatureModel = new FaceFeatureModel();
        faceFeatureModel.init(feature, poolSize);
        return faceFeatureModel;
    }

    @Bean
    public MilvusEngine milvusEngine() {
        MilvusEngine milvusEngine = new MilvusEngine();
        milvusEngine.init(host, port, collectionName, partitionName, indexType, metricType, dimension, nlist);
        return milvusEngine;
    }
}