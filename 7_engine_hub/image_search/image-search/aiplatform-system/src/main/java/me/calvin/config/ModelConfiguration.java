package me.calvin.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.calvin.modules.search.common.utils.face.FaceDetectionModel;
import me.calvin.modules.search.common.utils.face.FaceFeatureModel;
import me.calvin.modules.search.common.utils.feature.CommonFeatureModel;
import me.calvin.modules.search.common.utils.feature.CustFeatureModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FileProperties properties;

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

    //Image Model
    @Value("${image.feature}")
    private String commonFeature;

    @Value("${newModel.enabled}")
    private boolean newModel;

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

    @Bean
    public CommonFeatureModel commonFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        CommonFeatureModel commonFeatureModel = new CommonFeatureModel();
        commonFeatureModel.init(commonFeature);
        return commonFeatureModel;
    }

    @Bean
    public CustFeatureModel custFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        CustFeatureModel custFeatureModel = new CustFeatureModel();
        if (this.newModel) {
            custFeatureModel.init(properties.getPath().getNewModelPath(), commonFeature);
        }
        return custFeatureModel;
    }
}