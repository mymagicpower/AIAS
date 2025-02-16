package top.aias.platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.mlsd.MlsdSquareModel;
import top.aias.platform.model.ocr.RecognitionModel;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigOcr {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    // ocr model
    @Value("${model.ocrv4.det}")
    private String ocrDet;
    @Value("${model.ocrv4.rec}")
    private String ocrRec;
    @Value("${model.mlsd.model}")
    private String mlsd;


    @Bean
    public RecognitionModel recognitionModel() {
        RecognitionModel recognitionModel = new RecognitionModel(ocrDet, ocrRec, poolSize);
        return recognitionModel;
    }

    @Bean
    public MlsdSquareModel mlsdSquareModel() {
        MlsdSquareModel mlsdSquareModel = new MlsdSquareModel(mlsd, poolSize);

        if (loadMode.equalsIgnoreCase("eager")) {
            mlsdSquareModel.ensureInitialized();
        }
        return mlsdSquareModel;
    }
}