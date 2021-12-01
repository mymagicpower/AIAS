package me.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.infer.face.FaceDetectionModel;
import me.aias.infer.face.FaceFeatureModel;
import me.aias.infer.ocr.RecognitionModel;
import org.apache.commons.lang3.StringUtils;
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
    @Value("${model.face.det}")
    private String faceDet;
    @Value("${model.face.feature}")
    private String feature;
    @Value("${model.face.topK}")
    private int topK;
    @Value("${model.face.confThresh}")
    private float confThresh;
    @Value("${model.face.nmsThresh}")
    private float nmsThresh;

    //OCR Model
    @Value("${model.type}")
    private String type;
    // mobile model
    @Value("${model.mobile.det}")
    private String mobileDet;
    @Value("${model.mobile.rec}")
    private String mobileRec;
    // light model
    @Value("${model.light.det}")
    private String lightDet;
    @Value("${model.light.rec}")
    private String lightRec;
    // server model
    @Value("${model.server.det}")
    private String serverDet;
    @Value("${model.server.rec}")
    private String serverRec;

    @Bean
    public RecognitionModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        RecognitionModel recognitionModel = new RecognitionModel();
        if (StringUtils.isEmpty(type) || type.toLowerCase().equals("mobile")) {
            recognitionModel.init(mobileDet, mobileRec);
        } else if (type.toLowerCase().equals("light")) {
            recognitionModel.init(lightDet, lightRec);
        } else if (type.toLowerCase().equals("server")) {
            recognitionModel.init(serverDet, serverRec);
        } else {
            recognitionModel.init(mobileDet, mobileRec);
        }
        return recognitionModel;
    }

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