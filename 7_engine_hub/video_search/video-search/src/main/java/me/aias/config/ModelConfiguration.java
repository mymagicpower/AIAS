package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.common.face.FaceDetectionModel;
import me.aias.common.face.FaceFeatureModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 图片操作常量类
 * @author Calvin
 * @date 2021-12-12
 **/
@Configuration
public class ModelConfiguration {
    //Face Model
    @Value("${face.det}")
    private String faceDet;
    @Value("${face.feature}")
    private String feature;
    @Value("${face.shrink}")
    private float shrink;
    @Value("${face.threshold}")
    private float threshold;

    @Bean
    public FaceDetectionModel faceDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceDetectionModel faceDetectionModel = new FaceDetectionModel();
        faceDetectionModel.init(faceDet, shrink, threshold);
        return faceDetectionModel;
    }

    @Bean
    public FaceFeatureModel faceFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceFeatureModel faceFeatureModel = new FaceFeatureModel();
        faceFeatureModel.init(feature);
        return faceFeatureModel;
    }
}