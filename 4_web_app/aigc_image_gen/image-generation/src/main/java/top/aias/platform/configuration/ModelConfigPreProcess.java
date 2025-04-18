package top.aias.platform.configuration;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    // 模型根路径
    @Value("${model.modelPath}")
    private String modelPath;
    private int detect_resolution = 512;
    private int image_resolution = 512;

    // 深度估计 - dpt 模型
    @Bean
    public DptDepthModel dptDepthModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("dpt_depth.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        DptDepthModel model = new DptDepthModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 深度估计 - midas 模型
    @Bean
    public MidasDepthModel midasDepthModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("midas_depth.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        MidasDepthModel model = new MidasDepthModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 人体关键点
    @Bean
    public BodyModel bodyModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("body.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        BodyModel model = new BodyModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 人脸关键点
    @Bean
    public FaceModel faceModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("face.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        FaceModel model = new FaceModel(fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 手部关键点
    @Bean
    public HandModel handModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("hand.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        HandModel model = new HandModel(fullPath.toString(), poolSize, device);

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

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("body.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        PoseModel model = new PoseModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - lineart 模型
    @Bean
    public LineArtModel lineArtModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("lineart.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        LineArtModel model = new LineArtModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - lineart 动漫模型
    @Bean
    public LineArtAnimeModel lineArtAnimeModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("lineart_anime.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        LineArtAnimeModel model = new LineArtAnimeModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - lineart 粗糙线条模型
    @Bean
    public LineArtCoarseModel lineArtCoarseModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("lineart_coarse.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        LineArtCoarseModel model = new LineArtCoarseModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - 建筑线条
    @Bean
    public MlsdModel mlsdModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("mlsd.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        MlsdModel model = new MlsdModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - 法向图
    @Bean
    public NormalBaeModel normalBaeModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("normalbae.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        NormalBaeModel model = new NormalBaeModel(detect_resolution, image_resolution, fullPath.toString(), poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 线条轮廓 - 图像语义分割
    @Bean
    public SegUperNetModel segUperNetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("upernet.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        SegUperNetModel model = new SegUperNetModel(detect_resolution, image_resolution, fullPath.toString(), false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 边缘检测 - hed 模型
    @Bean
    public HedModel hedModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("hed.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        HedModel model = new HedModel(detect_resolution, image_resolution, fullPath.toString(), false, poolSize, device);

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

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get("hed.pt");
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        HedScribbleModel model = new HedScribbleModel(detect_resolution, image_resolution, fullPath.toString(), false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    // 边缘检测 - pidi 模型
    @Bean
    public PidiNetModel pidiNetModel() {
        Device device;
        String modelName;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelName = "pidi_cpu.pt";
        } else {
            device = Device.gpu();
            modelName = "pidi_gpu.pt";
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get(modelName);
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        PidiNetModel model = new PidiNetModel(detect_resolution, image_resolution, fullPath.toString(), false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }

    @Bean
    public PidiNetScribbleModel pidiNetScribbleModel() {
        Device device;
        String modelName;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
            modelName = "pidi_cpu.pt";
        } else {
            device = Device.gpu();
            modelName = "pidi_gpu.pt";
        }

        // 使用 Paths.get() 获取 Path 对象
        Path p1 = Paths.get(modelPath);
        Path p2 = Paths.get("controlnet");
        Path p3 = Paths.get(modelName);
        // 拼接路径
        Path fullPath = p1.resolve(p2).resolve(p3);

        PidiNetScribbleModel model = new PidiNetScribbleModel(detect_resolution, image_resolution, fullPath.toString(), false, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            model.ensureInitialized();
        }

        return model;
    }
}