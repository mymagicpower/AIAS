package top.aias.platform.configuration;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.model.asr.WhisperModel;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.mlsd.MlsdSquareModel;
import top.aias.platform.model.ocr.RecognitionModel;
import top.aias.platform.model.seg.FaceSegModel;
import top.aias.platform.model.sr.SrModel;
import top.aias.platform.model.trans.NllbModel;

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
    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String device;

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

    // 图像高清
    // 模型路径
    @Value("${model.sr.modelPath}")
    private String srModelPath;
    // 人脸检测模型
    @Value("${model.sr.faceModelName}")
    private String faceModelName;
    // 人像分割模型
    @Value("${model.sr.faceSegModelName}")
    private String faceSegModelName;
    // 人脸修复模型
    @Value("${model.sr.faceGanModelName}")
    private String faceGanModelName;
    // 图像超分模型
    @Value("${model.sr.srModelName}")
    private String srModelName;
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

    @Bean
    public FaceDetModel faceDetModel() throws IOException, ModelException {
        FaceDetModel faceDetModel = new FaceDetModel();
        if(device.equalsIgnoreCase("cpu")){
            faceDetModel.init(srModelPath, faceModelName, poolSize, Device.cpu());
        }else {
            faceDetModel.init(srModelPath, faceModelName, poolSize, Device.gpu());
        }
        return faceDetModel;
    }

    @Bean
    public FaceSegModel faceSegModel() throws IOException, ModelException {
        FaceSegModel faceSegModel = new FaceSegModel();
        if(device.equalsIgnoreCase("cpu")){
            faceSegModel.init(srModelPath, faceSegModelName, poolSize, Device.cpu());
        }else {
            faceSegModel.init(srModelPath, faceSegModelName, poolSize, Device.gpu());
        }
        return faceSegModel;
    }

    @Bean
    public FaceGanModel faceGanModel() throws IOException, ModelException {
        FaceGanModel faceGanModel = new FaceGanModel();
        if(device.equalsIgnoreCase("cpu")){
            faceGanModel.init(srModelPath, faceGanModelName, poolSize, Device.cpu());
        }else {
            faceGanModel.init(srModelPath, faceGanModelName, poolSize, Device.gpu());
        }
        return faceGanModel;
    }

    @Bean
    public SrModel srModel() throws IOException, ModelException {
        SrModel srModel = new SrModel();
        if(device.equalsIgnoreCase("cpu")){
            srModel.init(srModelPath, srModelName, poolSize, Device.cpu());
        }else {
            srModel.init(srModelPath, srModelName, poolSize, Device.gpu());
        }
        return srModel;
    }
}