package top.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import top.aias.face.detection.FaceDetectionModel;
import top.aias.face.feature.FaceFeatureModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Configuration
public class ModelConfiguration {
    //人脸检测大模型 - retinaOnnxCriteria
    @Value("${face.retinaface}")
    private String retinafaceModel;
    //人脸检测小模型 - mobileOnnxCriteria
    @Value("${face.mobilenet}")
    private String mobilenetModel;
    //人脸特征模型
    @Value("${face.feature}")
    private String featureModel;
    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    @Bean
    public FaceDetectionModel faceDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceDetectionModel faceDetModel = new FaceDetectionModel();
        faceDetModel.init(mobilenetModel, poolSize);
        return faceDetModel;
    }

    @Bean
    public FaceFeatureModel faceFeatureModel() throws IOException, ModelNotFoundException, MalformedModelException {
        FaceFeatureModel faceFeatureModel = new FaceFeatureModel();
        faceFeatureModel.init(featureModel, poolSize);
        return faceFeatureModel;
    }
}