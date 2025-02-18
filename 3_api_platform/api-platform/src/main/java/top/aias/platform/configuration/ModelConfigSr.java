package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.seg.FaceSegModel;
import top.aias.platform.model.sr.SrModel;

import java.io.File;
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
public class ModelConfigSr {
    @Value("${model.loadMode}")
    private String loadMode;

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    // 连接池大小
    @Value("${model.poolSize}")
    private int poolSize;

    // 图像高清
    // 模型根路径
    @Value("${model.modelPath}")
    private String modelPath;

    @Bean
    public FaceDetModel faceDetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 拼接路径
        String fullModelPath = modelPath + "sr" + File.separator;

        FaceDetModel faceDetModel = new FaceDetModel(fullModelPath, "retinaface_traced_model.pt", poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceDetModel.ensureInitialized();
        }

        return faceDetModel;
    }

    @Bean
    public FaceSegModel faceSegModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 拼接路径
        String fullModelPath = modelPath + "sr" + File.separator;

        FaceSegModel faceSegModel = new FaceSegModel(fullModelPath, "parsenet_traced_model.pt", poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceSegModel.ensureInitialized();
        }

        return faceSegModel;
    }

    @Bean
    public FaceGanModel faceGanModel() {
//        Device device;
//        if (deviceType.equalsIgnoreCase("cpu")) {
//            device = Device.cpu();
//        } else {
//            device = Device.gpu();
//        }

        // 拼接路径
        String fullModelPath = modelPath + "sr" + File.separator;

        // 模型只支持CPU，需要 GPU 导出新模型
        FaceGanModel faceGanModel = new FaceGanModel(fullModelPath, "gfpgan_traced_model.pt", poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            faceGanModel.ensureInitialized();
        }

        return faceGanModel;
    }

    @Bean
    public SrModel srModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 拼接路径
        String fullModelPath = modelPath + "sr" + File.separator;

        SrModel srModel = new SrModel(fullModelPath, "realsr_traced_model.pt", poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            srModel.ensureInitialized();
        }

        return srModel;
    }
}