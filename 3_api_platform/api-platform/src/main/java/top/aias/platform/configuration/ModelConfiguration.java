package top.aias.platform.configuration;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.model.asr.WhisperModel;
import top.aias.platform.model.color.DdcolorModel;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.mlsd.MlsdSquareModel;
import top.aias.platform.model.ocr.RecognitionModel;
import top.aias.platform.model.seg.*;
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
@Component
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

    // 模型路径
    @Value("${model.seg.modelPath}")
    private String segModelPath;
    // 通用分割模型
    @Value("${model.seg.bigModelName}")
    private String bigModelName;
    @Value("${model.seg.middleModelName}")
    private String middleModelName;
    @Value("${model.seg.smallModelName}")
    private String smallModelName;
    // 人体分割模型
    @Value("${model.seg.humanModelName}")
    private String humanModelName;
    // 动漫分割模型
    @Value("${model.seg.animeModelName}")
    private String animeModelName;
    // 衣服分割模型
    @Value("${model.seg.clothModelName}")
    private String clothModelName;
    // Sam2分割模型
    @Value("${model.seg.sam2.encoder}")
    private String encoder;
    @Value("${model.seg.sam2.decoder}")
    private String decoder;
    @Value("${model.seg.mask}")
    private boolean mask;

    // 黑白照片上色
    @Value("${model.color.modelPath}")
    private String colorModelPath;
    @Value("${model.color.modelName}")
    private String colorModelName;

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
        NllbModel textEncoderModel = new NllbModel();

        if(device.equalsIgnoreCase("cpu")){
            config.setGpu(false);
            textEncoderModel.init(config, modelPath, modelName, poolSize, Device.cpu());
        }else {
            config.setGpu(true);
            textEncoderModel.init(config, modelPath, modelName, poolSize, Device.gpu());
        }

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

    @Bean
    public BigUNetModel bigUNetModel() throws IOException, ModelException {
        BigUNetModel bigUNetModel = new BigUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            bigUNetModel.init(segModelPath, bigModelName, poolSize, mask, Device.cpu());
        }else {
            bigUNetModel.init(segModelPath, bigModelName, poolSize, mask, Device.gpu());
        }
        return bigUNetModel;
    }

    @Bean
    public MidUNetModel midUNetModel() throws IOException, ModelException {
        MidUNetModel midUNetModel = new MidUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            midUNetModel.init(segModelPath, middleModelName, poolSize, mask, Device.cpu());
        }else {
            midUNetModel.init(segModelPath, middleModelName, poolSize, mask, Device.gpu());
        }
        return midUNetModel;
    }

    @Bean
    public SmallUNetModel smallUNetModel() throws IOException, ModelException {
        SmallUNetModel smallUNetModel = new SmallUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            smallUNetModel.init(segModelPath, smallModelName, poolSize, mask, Device.cpu());
        }else {
            smallUNetModel.init(segModelPath, smallModelName, poolSize, mask, Device.gpu());
        }
        return smallUNetModel;
    }

    @Bean
    public UNetHumanSegModel uNetHumanSegModel() throws IOException, ModelException {
        UNetHumanSegModel uNetHumanSegModel = new UNetHumanSegModel();
        if(device.equalsIgnoreCase("cpu")){
            uNetHumanSegModel.init(segModelPath, humanModelName, poolSize, mask, Device.cpu());
        }else {
            uNetHumanSegModel.init(segModelPath, humanModelName, poolSize, mask, Device.gpu());
        }
        return uNetHumanSegModel;
    }

    @Bean
    public IsNetModel isNetModel() throws IOException, ModelException {
        IsNetModel isNetModel = new IsNetModel();
        if(device.equalsIgnoreCase("cpu")){
            isNetModel.init(segModelPath, animeModelName, poolSize, mask, Device.cpu());
        }else {
            isNetModel.init(segModelPath, animeModelName, poolSize, mask, Device.gpu());
        }
        return isNetModel;
    }
    @Bean
    public UNetClothSegModel uNetClothSegModel() throws IOException, ModelException {
        UNetClothSegModel uNetClothSegModel = new UNetClothSegModel();
        if(device.equalsIgnoreCase("cpu")){
            // clothCategory 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
            uNetClothSegModel.init(segModelPath, clothModelName,4, poolSize, Device.cpu());
        }else {
            uNetClothSegModel.init(segModelPath, clothModelName, 4, poolSize, Device.gpu());
        }
        return uNetClothSegModel;
    }
    @Bean
    public Sam2EncoderModel sam2EncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        Sam2EncoderModel sam2EncoderModel = new Sam2EncoderModel();
        if(device.equalsIgnoreCase("cpu")){
            sam2EncoderModel.init(segModelPath, encoder, poolSize, Device.cpu());
        }else {
            sam2EncoderModel.init(segModelPath, encoder, poolSize, Device.gpu());
        }
        return sam2EncoderModel;
    }

    @Bean
    public Sam2DecoderModel sam2DecoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        Sam2DecoderModel sam2DecoderModel = new Sam2DecoderModel();
        if(device.equalsIgnoreCase("cpu")){
            sam2DecoderModel.init(segModelPath, decoder, poolSize, Device.cpu());
        }else {
            sam2DecoderModel.init(segModelPath, decoder, poolSize, Device.gpu());
        }
        return sam2DecoderModel;
    }

    @Bean
    public DdcolorModel ddcolorModel() throws IOException, ModelException {
        DdcolorModel ddcolorModel = new DdcolorModel();
        ddcolorModel.init(colorModelPath, colorModelName, poolSize, Device.cpu());
        return ddcolorModel;
    }
}