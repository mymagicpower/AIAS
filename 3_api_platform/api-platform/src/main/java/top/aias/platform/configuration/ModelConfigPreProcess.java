package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.preprocess.depth.DptDepthModel;
import top.aias.platform.model.preprocess.depth.MidasDepthModel;
import top.aias.platform.model.preprocess.edge.HedModel;
import top.aias.platform.model.preprocess.edge.HedScribbleModel;
import top.aias.platform.model.preprocess.edge.PidiNetModel;
import top.aias.platform.model.preprocess.edge.PidiNetScribbleModel;
import top.aias.platform.model.preprocess.lineart.LineArtAnimeModel;
import top.aias.platform.model.preprocess.lineart.LineArtCoarseModel;
import top.aias.platform.model.preprocess.lineart.LineArtModel;
import top.aias.platform.model.preprocess.lineart.MlsdModel;
import top.aias.platform.model.preprocess.normal.NormalBaeModel;
import top.aias.platform.model.preprocess.pose.BodyModel;
import top.aias.platform.model.preprocess.pose.FaceModel;
import top.aias.platform.model.preprocess.pose.HandModel;
import top.aias.platform.model.preprocess.pose.PoseModel;
import top.aias.platform.model.preprocess.seg.SegUperNetModel;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigPreProcess {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    // 深度估计 - dpt 模型
    @Value("${model.preprocess.dptDepthModelPath}")
    private String dptDepthModelPath;

    // 深度估计 - midas 模型
    @Value("${model.preprocess.midasDepthModelPath}")
    private String midasDepthModelPath;

    // 人体关键点
    @Value("${model.preprocess.bodyModelPath}")
    private String bodyModelPath;

    // 人脸关键点
    @Value("${model.preprocess.faceModelPath}")
    private String faceModelPath;

    // 手部关键点
    @Value("${model.preprocess.handModelPath}")
    private String handModelPath;

    // 边缘检测 - hed 模型
    @Value("${model.preprocess.hedModelPath}")
    private String hedModelPath;

    // 边缘检测 - pidiCpu 模型
    @Value("${model.preprocess.pidiCpuModelPath}")
    private String pidiCpuModelPath;

    // 边缘检测 - pidiGpu 模型
    @Value("${model.preprocess.pidiGpuModelPath}")
    private String pidiGpuModelPath;

    // 线条轮廓 - lineart 模型
    @Value("${model.preprocess.lineartModelPath}")
    private String lineartModelPath;

    // 线条轮廓 - lineart 动漫模型
    @Value("${model.preprocess.lineartAnimeModelPath}")
    private String lineartAnimeModelPath;

    // 线条轮廓 - lineart 粗糙线条模型
    @Value("${model.preprocess.lineartCoarseModelPath}")
    private String lineartCoarseModelPath;

    // 线条轮廓 - 建筑线条
    @Value("${model.preprocess.mlsdModelPath}")
    private String mlsdModelPath;

    // 线条轮廓 - 法向图
    @Value("${model.preprocess.normalbaeModelPath}")
    private String normalbaeModelPath;

    // 线条轮廓 - 图像语义分割
    @Value("${model.preprocess.upernetModelPath}")
    private String upernetModelPath;

    private int detect_resolution = 512;
    private int image_resolution = 512;

    @Bean
    public DptDepthModel dptDepthModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        DptDepthModel model = new DptDepthModel(detect_resolution, image_resolution, dptDepthModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public MidasDepthModel midasDepthModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        MidasDepthModel model = new MidasDepthModel(detect_resolution, image_resolution, midasDepthModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public BodyModel bodyModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        BodyModel model = new BodyModel(detect_resolution, image_resolution, bodyModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public FaceModel faceModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceModel model = new FaceModel(faceModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public HandModel handModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        HandModel model = new HandModel(handModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public PoseModel poseModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        PoseModel model = new PoseModel(detect_resolution, image_resolution, bodyModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public LineArtModel lineArtModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        LineArtModel model = new LineArtModel(detect_resolution, image_resolution, lineartModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public LineArtAnimeModel lineArtAnimeModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        LineArtAnimeModel model = new LineArtAnimeModel(detect_resolution, image_resolution, lineartAnimeModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public LineArtCoarseModel lineArtCoarseModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        LineArtCoarseModel model = new LineArtCoarseModel(detect_resolution, image_resolution, lineartCoarseModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }





    @Bean
    public MlsdModel mlsdModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        MlsdModel model = new MlsdModel(detect_resolution, image_resolution, mlsdModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public NormalBaeModel normalBaeModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        NormalBaeModel model = new NormalBaeModel(detect_resolution, image_resolution, normalbaeModelPath, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }


    @Bean
    public SegUperNetModel segUperNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SegUperNetModel model = new SegUperNetModel(detect_resolution, image_resolution, upernetModelPath, false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public HedModel hedModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        HedModel model = new HedModel(detect_resolution, image_resolution, hedModelPath, false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public HedScribbleModel hedScribbleModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        HedScribbleModel model = new HedScribbleModel(detect_resolution, image_resolution, hedModelPath, false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public PidiNetModel pidiNetModel() {
        Device device;
        String modelPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelPath = pidiCpuModelPath;
        } else {
            device = Device.gpu();
            modelPath = pidiGpuModelPath;
        }

        PidiNetModel model = new PidiNetModel(detect_resolution, image_resolution, modelPath, false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public PidiNetScribbleModel pidiNetScribbleModel() {
        Device device;
        String modelPath;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelPath = pidiCpuModelPath;
        } else {
            device = Device.gpu();
            modelPath = pidiGpuModelPath;
        }

        PidiNetScribbleModel model = new PidiNetScribbleModel(detect_resolution, image_resolution, modelPath, false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }
}