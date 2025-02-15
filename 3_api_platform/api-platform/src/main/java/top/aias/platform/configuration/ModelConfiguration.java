package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
import top.aias.platform.model.vad.SileroVadModel;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfiguration {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

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
    @Value("${model.asr.vad}")
    private String vadModelPath;
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
    @Value("${model.seg.mask}")
    private boolean mask;

    // 黑白照片上色
    @Value("${model.color.modelPath}")
    private String colorModelPath;
    @Value("${model.color.modelName}")
    private String colorModelName;

    @Bean
    public RecognitionModel recognitionModel() {
        RecognitionModel recognitionModel = new RecognitionModel(ocrDet, ocrRec, poolSize);
        return recognitionModel;
    }

    @Bean
    public SileroVadModel sileroVadModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SileroVadModel sileroVadModel = new SileroVadModel(vadModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            sileroVadModel.ensureInitialized();
        }

        return sileroVadModel;
    }

    @Bean
    public MlsdSquareModel mlsdSquareModel() {
        MlsdSquareModel mlsdSquareModel = new MlsdSquareModel(mlsd, poolSize);

        if (loadMode.equalsIgnoreCase("eager")) {
            mlsdSquareModel.ensureInitialized();
        }
        return mlsdSquareModel;
    }

    @Bean
    public WhisperModel whisperModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        int kvLength = 24;
        int encoderIndex = 32;

        String model = baseModel;

        switch (type) {
            case "tiny":
                kvLength = 16;
                encoderIndex = 22;
                model = tinyModel;
                break;
            case "base":
                kvLength = 24;
                encoderIndex = 32;
                model = baseModel;
                break;
            case "small":
                kvLength = 48;
                encoderIndex = 62;
                model = smallModel;
                break;
        }

        WhisperModel whisperModel = new WhisperModel(model, poolSize, device, kvLength, encoderIndex);

        if (loadMode.equalsIgnoreCase("eager")) {
            whisperModel.ensureInitialized();
        }

        return whisperModel;
    }

    @Bean
    public NllbModel textEncoderModel() {
        TransConfig config = new TransConfig();
        config.setMaxSeqLength(maxLength);

        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        NllbModel textEncoderModel = new NllbModel(config, modelPath, modelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            textEncoderModel.ensureInitialized();
        }

        return textEncoderModel;
    }

    @Bean
    public FaceDetModel faceDetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceDetModel faceDetModel = new FaceDetModel(srModelPath, faceModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceDetModel.ensureInitialized();
        }

        return faceDetModel;
    }

    @Bean
    public FaceSegModel faceSegModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceSegModel faceSegModel = new FaceSegModel(srModelPath, faceSegModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceSegModel.ensureInitialized();
        }

        return faceSegModel;
    }

    @Bean
    public FaceGanModel faceGanModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceGanModel faceGanModel = new FaceGanModel(srModelPath, faceGanModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceGanModel.ensureInitialized();
        }

        return faceGanModel;
    }

    @Bean
    public SrModel srModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SrModel srModel = new SrModel(srModelPath, srModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            srModel.ensureInitialized();
        }

        return srModel;
    }

    @Bean
    public BigUNetModel bigUNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        BigUNetModel bigUNetModel = new BigUNetModel(segModelPath, bigModelName, poolSize, mask, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            bigUNetModel.ensureInitialized();
        }

        return bigUNetModel;
    }

    @Bean
    public MidUNetModel midUNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        MidUNetModel midUNetModel = new MidUNetModel(segModelPath, middleModelName, poolSize, mask, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            midUNetModel.ensureInitialized();
        }

        return midUNetModel;
    }

    @Bean
    public SmallUNetModel smallUNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SmallUNetModel smallUNetModel = new SmallUNetModel(segModelPath, smallModelName, poolSize, mask, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            smallUNetModel.ensureInitialized();
        }

        return smallUNetModel;
    }

    @Bean
    public UNetHumanSegModel uNetHumanSegModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        UNetHumanSegModel uNetHumanSegModel = new UNetHumanSegModel(segModelPath, humanModelName, poolSize, mask, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            uNetHumanSegModel.ensureInitialized();
        }

        return uNetHumanSegModel;
    }

    @Bean
    public IsNetModel isNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        IsNetModel isNetModel = new IsNetModel(segModelPath, animeModelName, poolSize, mask, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            isNetModel.ensureInitialized();
        }

        return isNetModel;
    }

    @Bean
    public UNetClothSegModel uNetClothSegModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // clothCategory 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
        UNetClothSegModel uNetClothSegModel = new UNetClothSegModel(segModelPath, clothModelName, 4, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            uNetClothSegModel.ensureInitialized();
        }

        return uNetClothSegModel;
    }

    @Bean
    public DdcolorModel ddcolorModel() {
        DdcolorModel ddcolorModel = new DdcolorModel(colorModelPath, colorModelName, poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            ddcolorModel.ensureInitialized();
        }

        return ddcolorModel;
    }
}