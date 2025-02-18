package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.asr.WhisperModel;
import top.aias.platform.model.vad.SileroVadModel;

import java.io.File;

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
    // 模型根路径
    @Value("${model.modelPath}")
    private String modelPath;


    @Bean
    public SileroVadModel sileroVadModel() {
//        Device device;
//        if (deviceType.equalsIgnoreCase("cpu")) {
//            device = Device.cpu();
//        } else {
//            device = Device.gpu();
//        }

        // 拼接路径
        String fullModelPath = modelPath + "asr" + File.separator + "silero_vad.onnx";

        // TODO 需要导出支持GPU 的模型， 目前只支持 CPU
        SileroVadModel sileroVadModel = new SileroVadModel(fullModelPath, poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            sileroVadModel.ensureInitialized();
        }

        return sileroVadModel;
    }

    @Bean
    public WhisperModel whisperModel() {
//        Device device;
//        if (deviceType.equalsIgnoreCase("cpu")) {
//            device = Device.cpu();
//        } else {
//            device = Device.gpu();
//        }

        int kvLength = 24;
        int encoderIndex = 32;

        String modelName = "traced_whisper_base.pt";

        switch (type) {
            case "tiny":
                kvLength = 16;
                encoderIndex = 22;
                modelName = "traced_whisper_tiny.pt";
                break;
            case "base":
                kvLength = 24;
                encoderIndex = 32;
                modelName = "traced_whisper_base.pt";
                break;
            case "small":
                kvLength = 48;
                encoderIndex = 62;
                modelName = "traced_whisper_tiny.pt";
                break;
        }

        // 拼接路径
        String fullModelPath = modelPath + "asr" + File.separator + modelName;

        // TODO 需要导出支持GPU 的模型， 目前只支持 CPU
        WhisperModel whisperModel = new WhisperModel(fullModelPath, poolSize, Device.cpu(), kvLength, encoderIndex);

        if (loadMode.equalsIgnoreCase("eager")) {
            whisperModel.ensureInitialized();
        }

        return whisperModel;
    }
}