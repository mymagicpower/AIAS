package me.aias.ocr.configuration;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import me.aias.ocr.inference.LayoutDetectionModel;
import me.aias.ocr.inference.RecognitionModel;
import me.aias.ocr.inference.TableDetectionModel;
import me.aias.ocr.utils.Constants;
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
    @Value("${model.type}")
    private String type;

    @Value("${model.table.layout}")
    private String tableLayout;
    @Value("${model.table.table-en}")
    private String table;
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
    // v3 model
    @Value("${model.v3.det}")
    private String v3Det;
    @Value("${model.v3.rec}")
    private String v3Rec;

    @Bean
    public RecognitionModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        RecognitionModel recognitionModel = new RecognitionModel();
        if (StringUtils.isEmpty(type) || type.equalsIgnoreCase("v3")) {
            recognitionModel.init(v3Det, v3Rec);
        } else if (type.equalsIgnoreCase("light")) {
            recognitionModel.init(lightDet, lightRec);
        } else if (type.equalsIgnoreCase("server")) {
            recognitionModel.init(serverDet, serverRec);
        } else {
            recognitionModel.init(mobileDet, mobileRec);
        }
        return recognitionModel;
    }

    @Bean
    public TableDetectionModel tableDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TableDetectionModel tableDetectionModel = new TableDetectionModel();
        tableDetectionModel.init(table);
        return tableDetectionModel;
    }

    @Bean
    public LayoutDetectionModel layoutDetectionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        LayoutDetectionModel layoutDetectionModel = new LayoutDetectionModel();
        layoutDetectionModel.init(tableLayout);
        return layoutDetectionModel;
    }
}