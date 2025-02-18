package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.seg.*;

import java.io.File;

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
    @Value("${model.modelPath}")
    private String modelPath;
    @Value("${model.seg.mask}")
    private boolean mask;


    @Bean
    public BigUNetModel bigUNetModel() {

        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        BigUNetModel bigUNetModel = new BigUNetModel(fullModelPath, "u2net.onnx", poolSize, mask, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            bigUNetModel.ensureInitialized();
        }

        return bigUNetModel;
    }

    @Bean
    public MidUNetModel midUNetModel() {

        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        MidUNetModel midUNetModel = new MidUNetModel(fullModelPath, "silueta.onnx", poolSize, mask, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            midUNetModel.ensureInitialized();
        }

        return midUNetModel;
    }

    @Bean
    public SmallUNetModel smallUNetModel() {
        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        SmallUNetModel smallUNetModel = new SmallUNetModel(fullModelPath, "u2netp.onnx", poolSize, mask, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            smallUNetModel.ensureInitialized();
        }

        return smallUNetModel;
    }

    @Bean
    public UNetHumanSegModel uNetHumanSegModel() {

        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        UNetHumanSegModel uNetHumanSegModel = new UNetHumanSegModel(fullModelPath, "human.onnx", poolSize, mask, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            uNetHumanSegModel.ensureInitialized();
        }

        return uNetHumanSegModel;
    }

    @Bean
    public IsNetModel isNetModel() {

        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        IsNetModel isNetModel = new IsNetModel(fullModelPath, "anime.onnx", poolSize, mask, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            isNetModel.ensureInitialized();
        }

        return isNetModel;
    }

    @Bean
    public UNetClothSegModel uNetClothSegModel() {

        // 拼接路径
        String fullModelPath = modelPath + "seg" + File.separator;

        // clothCategory 4个值: 1,2,3,4  (1 上半身， 2 下半身, 3 连体衣, 4 所有）
        UNetClothSegModel uNetClothSegModel = new UNetClothSegModel(fullModelPath, "cloth.onnx", 4, poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            uNetClothSegModel.ensureInitialized();
        }

        return uNetClothSegModel;
    }
}