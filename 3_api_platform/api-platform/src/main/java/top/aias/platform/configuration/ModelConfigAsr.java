package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.asr.WhisperModel;
import top.aias.platform.model.vad.SileroVadModel;

/**
 * ASR 语音识别 - 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigAsr {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
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
}