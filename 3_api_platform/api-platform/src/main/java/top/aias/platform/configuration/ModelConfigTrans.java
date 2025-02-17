package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.model.trans.NllbModel;

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
    @Value("${model.translation.modelPath}")
    private String modelPath;
    @Value("${model.translation.modelName}")
    private String modelName;
    // 输出文字最大长度
    @Value("${config.maxLength}")
    private int maxLength;

    @Bean
    public NllbModel nllbModel() {
        TransConfig config = new TransConfig();
        config.setMaxSeqLength(maxLength);

        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        NllbModel nllbModel = new NllbModel(config, modelPath, modelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            nllbModel.ensureInitialized();
        }

        return nllbModel;
    }
}