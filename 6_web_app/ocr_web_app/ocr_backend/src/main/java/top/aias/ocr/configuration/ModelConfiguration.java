package top.aias.ocr.configuration;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.ocr.model.LayoutDetectionModel;
import top.aias.ocr.model.MlsdSquareModel;
import top.aias.ocr.model.RecognitionModel;
import top.aias.ocr.model.TableRecognitionModel;

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
    @Value("${model.table.layout}")
    private String tableLayout;
    @Value("${model.table.rec}")
    private String table;
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
    public TableRecognitionModel tableDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TableRecognitionModel tableRecognitionModel = new TableRecognitionModel();
        tableRecognitionModel.init(table, poolSize);
        return tableRecognitionModel;
    }

    @Bean
    public LayoutDetectionModel layoutDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        LayoutDetectionModel layoutDetectionModel = new LayoutDetectionModel();
        layoutDetectionModel.init(tableLayout, poolSize);
        return layoutDetectionModel;
    }

    @Bean
    public MlsdSquareModel mlsdSquareModel() throws IOException, ModelNotFoundException, MalformedModelException {
        MlsdSquareModel mlsdSquareModel = new MlsdSquareModel();
        mlsdSquareModel.init(mlsd, poolSize);
        return mlsdSquareModel;
    }
}