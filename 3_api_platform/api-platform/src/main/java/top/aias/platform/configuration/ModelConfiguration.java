package top.aias.platform.configuration;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.model.MlsdSquareModel;
import top.aias.platform.model.NllbModel;
import top.aias.platform.model.RecognitionModel;
import top.aias.platform.model.WhisperModel;

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

    // 语音识别
    @Value("${model.asr.type}")
    private String type;
    @Value("${model.asr.tiny}")
    private String tinyModel;
    @Value("${model.asr.base}")
    private String baseModel;
    @Value("${model.asr.small}")
    private String smallModel;

    // 翻译
    @Value("${model.translation.modelPath}")
    private String modelPath;
    @Value("${model.translation.modelName}")
    private String modelName;
    // 输出文字最大长度
    @Value("${config.maxLength}")
    private int maxLength;

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

    @Bean
    public WhisperModel whisperModel() throws IOException, ModelNotFoundException, MalformedModelException {
        WhisperModel whisperModel = new WhisperModel();

        int kvLength = 0;
        int encoderIndex = 0;

        switch (type) {
            case "tiny":
                kvLength = 16;
                encoderIndex = 22;
                whisperModel.init(tinyModel, poolSize, kvLength, encoderIndex);
                break;
            case "base":
                kvLength = 24;
                encoderIndex = 32;
                whisperModel.init(baseModel, poolSize, kvLength, encoderIndex);
                break;
            case "small":
                kvLength = 48;
                encoderIndex = 62;
                whisperModel.init(smallModel, poolSize, kvLength, encoderIndex);
                break;
        }

        return whisperModel;
    }

    @Bean
    public NllbModel textEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        TransConfig config = new TransConfig();
        config.setMaxSeqLength(maxLength);
        config.setGpu(false);

        NllbModel textEncoderModel = new NllbModel();
        textEncoderModel.init(config, modelPath, modelName, poolSize, Device.cpu());
        return textEncoderModel;
    }
}