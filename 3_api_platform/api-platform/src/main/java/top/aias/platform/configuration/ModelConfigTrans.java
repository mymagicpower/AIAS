package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.model.trans.NllbModel;

import java.io.File;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigTrans {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;


    // 翻译
    // 模型根路径
    @Value("${model.modelPath}")
    private String modelPath;

    // 输出文字最大长度
    @Value("${config.maxLength}")
    private int maxLength;

    @Bean
    public NllbModel nllbModel() {
        TransConfig config = new TransConfig();
        config.setMaxSeqLength(maxLength);

        Device device;
        String modelName;

        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelName = "traced_translation_cpu.pt";
        } else {
            device = Device.gpu();
            modelName = "traced_translation_gpu.pt";
        }

        // 拼接路径
        String fullModelPath = modelPath + "trans" + File.separator;

        NllbModel nllbModel = new NllbModel(config, fullModelPath, modelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            nllbModel.ensureInitialized();
        }

        return nllbModel;
    }
}