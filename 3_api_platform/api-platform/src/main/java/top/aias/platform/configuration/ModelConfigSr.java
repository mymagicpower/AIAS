package top.aias.platform.configuration;

import ai.djl.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.det.FaceDetModel;
import top.aias.platform.model.gan.FaceGanModel;
import top.aias.platform.model.seg.FaceSegModel;
import top.aias.platform.model.sr.SrModel;

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
    // 模型路径
    @Value("${model.sr.modelPath}")
    private String srModelPath;
    // 人脸检测模型
    @Value("${model.sr.faceModelName}")
    private String faceModelName;
    // 人像分割模型
    @Value("${model.sr.faceSegModelName}")
    private String faceSegModelName;
    // 人脸修复模型
    @Value("${model.sr.faceGanModelName}")
    private String faceGanModelName;
    // 图像超分模型
    @Value("${model.sr.srModelName}")
    private String srModelName;

    @Bean
    public FaceDetModel faceDetModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceDetModel faceDetModel = new FaceDetModel(srModelPath, faceModelName, poolSize, device);

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

        FaceSegModel faceSegModel = new FaceSegModel(srModelPath, faceSegModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            faceSegModel.ensureInitialized();
        }

        return faceSegModel;
    }

    @Bean
    public FaceGanModel faceGanModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        FaceGanModel faceGanModel = new FaceGanModel(srModelPath, faceGanModelName, poolSize, device);

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

        SrModel srModel = new SrModel(srModelPath, srModelName, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            srModel.ensureInitialized();
        }

        return srModel;
    }
}