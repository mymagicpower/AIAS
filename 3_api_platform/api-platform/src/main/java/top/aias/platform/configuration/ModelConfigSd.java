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

    // encoder, decoder 模型
    @Value("${model.sd.textEncoder}")
    private String textEncoder;
    @Value("${model.sd.vaeEncoder}")
    private String vaeEncoder;
    @Value("${model.sd.vaeDecoder}")
    private String vaeDecoder;

    // Unet 模型
    @Value("${model.sd.unet}")
    private String unet;
    @Value("${model.sd.controlnetUnet}")
    private String controlnetUnet;

    // ControlNet 模型名称
    @Value("${model.sd.canny}")
    private String canny;
    @Value("${model.sd.depth}")
    private String depth;
    @Value("${model.sd.ip2p}")
    private String ip2p;
    @Value("${model.sd.lineart}")
    private String lineart;
    @Value("${model.sd.lineartAnime}")
    private String lineartAnime;
    @Value("${model.sd.mlsd}")
    private String mlsd;
    @Value("${model.sd.normalbae}")
    private String normalbae;
    @Value("${model.sd.openpose}")
    private String openpose;
    @Value("${model.sd.scribble}")
    private String scribble;
    @Value("${model.sd.seg}")
    private String seg;
    @Value("${model.sd.shuffle}")
    private String shuffle;
    @Value("${model.sd.softedge}")
    private String softedge;

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

        SdCannyModel model = new SdCannyModel(modelRootPath, canny, poolSize, device);

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

        SdDepthModel model = new SdDepthModel(modelRootPath, depth, poolSize, device);

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

        SdLineartAnimeModel model = new SdLineartAnimeModel(modelRootPath, lineartAnime, poolSize, device);

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

        SdLineartModel model = new SdLineartModel(modelRootPath, lineart, poolSize, device);

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

        SdMlsdModel model = new SdMlsdModel(modelRootPath, mlsd, poolSize, device);

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

        SdNormalBaeModel model = new SdNormalBaeModel(modelRootPath, normalbae, poolSize, device);

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

        SdOpenPoseModel model = new SdOpenPoseModel(modelRootPath, openpose, poolSize, device);

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

        SdP2PModel model = new SdP2PModel(modelRootPath, ip2p, poolSize, device);

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

        SdScribbleModel model = new SdScribbleModel(modelRootPath, scribble, poolSize, device);

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

        SdSegModel model = new SdSegModel(modelRootPath, seg, poolSize, device);

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

        SdShuffleModel model = new SdShuffleModel(modelRootPath, shuffle, poolSize, device);

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

        SdSoftEdgeModel model = new SdSoftEdgeModel(modelRootPath, softedge, poolSize, device);

//        if (loadMode.equalsIgnoreCase("eager")) {
//            ddcolorModel.ensureInitialized();
//        }

        return model;
    }
}