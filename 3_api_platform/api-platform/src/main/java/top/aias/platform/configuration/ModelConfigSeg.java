package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.seg.*;

/**
 * 图像分割 - 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigSeg {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

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
}