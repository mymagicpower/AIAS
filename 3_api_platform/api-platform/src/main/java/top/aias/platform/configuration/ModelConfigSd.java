package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.color.DdcolorModel;
import top.aias.platform.model.sd.controlnet.*;
import top.aias.platform.model.sd.text.TextEncoderModel;
import top.aias.platform.model.sd.unet.UNetForControlModel;
import top.aias.platform.model.sd.unet.UNetModel;
import top.aias.platform.model.sd.vae.VaeDecoderModel;
import top.aias.platform.model.sd.vae.VaeEncoderModel;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigSd {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    // 模型路径
    @Value("${model.sd.cpuModelPath}")
    private String cpuModelPath;
    @Value("${model.sd.gpuModelPath}")
    private String gpuModelPath;

    @Bean
    public TextEncoderModel textEncoderModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        TextEncoderModel model = new TextEncoderModel(modelRootPath, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public VaeEncoderModel vaeEncoderModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        VaeEncoderModel model = new VaeEncoderModel(modelRootPath, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public VaeDecoderModel vaeDecoderModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        VaeDecoderModel model = new VaeDecoderModel(modelRootPath, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public UNetModel unetModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        UNetModel model = new UNetModel(modelRootPath, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public UNetForControlModel uNetForControlModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        UNetForControlModel model = new UNetForControlModel(modelRootPath, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdCannyModel sdCannyModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdCannyModel model = new SdCannyModel(modelRootPath, "controlnet_canny.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdDepthModel sdDepthModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdDepthModel model = new SdDepthModel(modelRootPath, "controlnet_depth.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdLineartAnimeModel sdLineartAnimeModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdLineartAnimeModel model = new SdLineartAnimeModel(modelRootPath, "controlnet_lineart_anime.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdLineartModel sdLineartModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdLineartModel model = new SdLineartModel(modelRootPath, "controlnet_lineart.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdMlsdModel sdMlsdModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdMlsdModel model = new SdMlsdModel(modelRootPath, "controlnet_mlsd.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdNormalBaeModel sdNormalBaeModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdNormalBaeModel model = new SdNormalBaeModel(modelRootPath, "controlnet_normalbae.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdOpenPoseModel sdOpenPoseModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdOpenPoseModel model = new SdOpenPoseModel(modelRootPath, "controlnet_openpose.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdP2PModel sdP2PModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdP2PModel model = new SdP2PModel(modelRootPath, "controlnet_ip2p.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdScribbleModel sdScribbleModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdScribbleModel model = new SdScribbleModel(modelRootPath, "controlnet_scribble.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdSegModel sdSegModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdSegModel model = new SdSegModel(modelRootPath, "controlnet_seg.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdShuffleModel sdShuffleModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdShuffleModel model = new SdShuffleModel(modelRootPath, "controlnet_shuffle.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }

    @Bean
    public SdSoftEdgeModel sdSoftEdgeModel() {
        Device device;
        String modelRootPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelRootPath = cpuModelPath;
        } else {
            device = Device.gpu();
            modelRootPath = gpuModelPath;
        }

        SdSoftEdgeModel model = new SdSoftEdgeModel(modelRootPath, "controlnet_softedge.pt", poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }
}