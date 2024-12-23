package top.aias.config;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.generate.SearchConfig;
import top.aias.model.NllbModel;

import java.io.IOException;

/**
 * 模型配置类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
@Configuration
public class ModelConfiguration {
    // Text Model
    @Value("${model.modelPath}")
    private String modelPath;
    @Value("${model.modelName}")
    private String modelName;
    //连接池大小
    @Value("${model.poolSize}")
    private int poolSize;
    // 输出文字最大长度
    @Value("${config.maxLength}")
    private int maxLength;
    @Value("${config.gpu}")
    private boolean gpu;
    @Bean
    public NllbModel textEncoderModel() throws IOException, ModelNotFoundException, MalformedModelException {
        SearchConfig config = new SearchConfig();
        config.setMaxSeqLength(maxLength);
        config.setGpu(gpu);

        NllbModel textEncoderModel = new NllbModel();
        Device device = Device.cpu();
        if(gpu){
            device = Device.gpu();
        }
        textEncoderModel.init(config, modelPath, modelName, poolSize, device);
        return textEncoderModel;
    }
}