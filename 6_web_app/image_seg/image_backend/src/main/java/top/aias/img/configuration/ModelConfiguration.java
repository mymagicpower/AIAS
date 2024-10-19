package top.aias.img.configuration;

import ai.djl.Device;
import ai.djl.ModelException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.img.model.*;

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
    // 模型路径
    @Value("${model.modelPath}")
    private String modelPath;
    // 通用分割模型
    @Value("${model.bigModelName}")
    private String bigModelName;
    @Value("${model.middleModelName}")
    private String middleModelName;
    @Value("${model.smallModelName}")
    private String smallModelName;
    // 人体分割模型
    @Value("${model.humanModelName}")
    private String humanModelName;
    // 动漫分割模型
    @Value("${model.animeModelName}")
    private String animeModelName;
    // 衣服分割模型
    @Value("${model.clothModelName}")
    private String clothModelName;

    // 最大设置为 CPU 核心数 (Core Number)
    @Value("${model.poolSize}")
    private int poolSize;
    @Value("${model.mask}")
    private boolean mask;
    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String device;
    @Bean
    public BigUNetModel bigUNetModel() throws IOException, ModelException {
        BigUNetModel bigUNetModel = new BigUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            bigUNetModel.init(modelPath, bigModelName, poolSize, mask, Device.cpu());
        }else {
            bigUNetModel.init(modelPath, bigModelName, poolSize, mask, Device.gpu());
        }
        return bigUNetModel;
    }

    @Bean
    public MidUNetModel midUNetModel() throws IOException, ModelException {
        MidUNetModel midUNetModel = new MidUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            midUNetModel.init(modelPath, middleModelName, poolSize, mask, Device.cpu());
        }else {
            midUNetModel.init(modelPath, middleModelName, poolSize, mask, Device.gpu());
        }
        return midUNetModel;
    }

    @Bean
    public SmallUNetModel smallUNetModel() throws IOException, ModelException {
        SmallUNetModel smallUNetModel = new SmallUNetModel();
        if(device.equalsIgnoreCase("cpu")){
            smallUNetModel.init(modelPath, smallModelName, poolSize, mask, Device.cpu());
        }else {
            smallUNetModel.init(modelPath, smallModelName, poolSize, mask, Device.gpu());
        }
        return smallUNetModel;
    }

    @Bean
    public UNetHumanSegModel uNetHumanSegModel() throws IOException, ModelException {
        UNetHumanSegModel uNetHumanSegModel = new UNetHumanSegModel();
        if(device.equalsIgnoreCase("cpu")){
            uNetHumanSegModel.init(modelPath, humanModelName, poolSize, mask, Device.cpu());
        }else {
            uNetHumanSegModel.init(modelPath, humanModelName, poolSize, mask, Device.gpu());
        }
        return uNetHumanSegModel;
    }

    @Bean
    public IsNetModel isNetModel() throws IOException, ModelException {
        IsNetModel isNetModel = new IsNetModel();
        if(device.equalsIgnoreCase("cpu")){
            isNetModel.init(modelPath, animeModelName, poolSize, mask, Device.cpu());
        }else {
            isNetModel.init(modelPath, animeModelName, poolSize, mask, Device.gpu());
        }
        return isNetModel;
    }
    @Bean
    public UNetClothSegModel uNetClothSegModel() throws IOException, ModelException {
        UNetClothSegModel uNetClothSegModel = new UNetClothSegModel();
        if(device.equalsIgnoreCase("cpu")){
            // clothCategory 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
            uNetClothSegModel.init(modelPath, clothModelName,4, poolSize, Device.cpu());
        }else {
            uNetClothSegModel.init(modelPath, clothModelName, 4, poolSize, Device.gpu());
        }
        return uNetClothSegModel;
    }
}