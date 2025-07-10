package top.aias.iocr.configuration;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import top.aias.iocr.model.MlsdSquareModel;
import top.aias.iocr.model.RecognitionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    // ocr model
    @Value("${model.ocrv4.det}")
    private String ocrDet;
    @Value("${model.ocrv4.rec}")
    private String ocrRec;
    @Value("${model.mlsd.model}")
    private String mlsd;
    @Value("${model.poolSize}")
    private int poolSize;


    @Bean
    public RecognitionModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        RecognitionModel recognitionModel = new RecognitionModel();
        recognitionModel.init(ocrDet, ocrRec, poolSize);
        return recognitionModel;
    }

    @Bean
    public MlsdSquareModel mlsdSquareModel() throws IOException, ModelNotFoundException, MalformedModelException {
        MlsdSquareModel mlsdSquareModel = new MlsdSquareModel();
        mlsdSquareModel.init(mlsd, poolSize);
        return mlsdSquareModel;
    }
}