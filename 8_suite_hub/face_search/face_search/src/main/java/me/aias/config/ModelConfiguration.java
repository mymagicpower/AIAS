package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.common.face.FaceDetectionModel;
import me.aias.common.face.FaceFeatureModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Configuration
public class ModelConfiguration {
    //Face Model
    @Value("${face.det}")
    private String faceDet;
    @Value("${face.feature}")
    private String feature;
    @Value("${face.topK}")
    private int topK;
    @Value("${face.confThresh}")
    private float confThresh;
    @Value("${face.nmsThresh}")
    private float nmsThresh;

    @Bean
    public FaceDetectionModel faceDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceDetectionModel faceDetectionModel = new FaceDetectionModel();
        faceDetectionModel.init(faceDet, topK, confThresh, nmsThresh);
        return faceDetectionModel;
    }

    @Bean
    public FaceFeatureModel faceFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceFeatureModel faceFeatureModel = new FaceFeatureModel();
        faceFeatureModel.init(feature);
        return faceFeatureModel;
    }
}