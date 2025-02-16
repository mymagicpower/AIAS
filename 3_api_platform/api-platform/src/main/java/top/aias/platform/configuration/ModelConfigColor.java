package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.color.DdcolorModel;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigColor {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    // 黑白照片上色
    @Value("${model.color.modelPath}")
    private String colorModelPath;
    @Value("${model.color.modelName}")
    private String colorModelName;

    @Bean
    public DdcolorModel ddcolorModel() {
        DdcolorModel ddcolorModel = new DdcolorModel(colorModelPath, colorModelName, poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            ddcolorModel.ensureInitialized();
        }

        return ddcolorModel;
    }
}