package top.aias.img.configuration;

import ai.djl.Device;
import ai.djl.ModelException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aias.img.model.*;
import top.aias.img.model.det.FaceDetModel;
import top.aias.img.model.gan.FaceGanModel;
import top.aias.img.model.seg.FaceSegModel;
import top.aias.img.model.sr.SrModel;

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
    // 人脸检测模型
    @Value("${model.faceModelName}")
    private String faceModelName;
    // 人像分割模型
    @Value("${model.faceSegModelName}")
    private String faceSegModelName;
    // 人脸修复模型
    @Value("${model.faceGanModelName}")
    private String faceGanModelName;
    // 图像超分模型
    @Value("${model.srModelName}")
    private String srModelName;

    // 最大设置为 CPU 核心数 (Core Number)
    @Value("${model.poolSize}")
    private int poolSize;
    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String device;

    @Bean
    public FaceDetModel faceDetModel() throws IOException, ModelException {
        FaceDetModel faceDetModel = new FaceDetModel();
        if(device.equalsIgnoreCase("cpu")){
            faceDetModel.init(modelPath, faceModelName, poolSize, Device.cpu());
        }else {
            faceDetModel.init(modelPath, faceModelName, poolSize, Device.gpu());
        }
        return faceDetModel;
    }

    @Bean
    public FaceSegModel faceSegModel() throws IOException, ModelException {
        FaceSegModel faceSegModel = new FaceSegModel();
        if(device.equalsIgnoreCase("cpu")){
            faceSegModel.init(modelPath, faceSegModelName, poolSize, Device.cpu());
        }else {
            faceSegModel.init(modelPath, faceSegModelName, poolSize, Device.gpu());
        }
        return faceSegModel;
    }

    @Bean
    public FaceGanModel faceGanModel() throws IOException, ModelException {
        FaceGanModel faceGanModel = new FaceGanModel();
        if(device.equalsIgnoreCase("cpu")){
            faceGanModel.init(modelPath, faceGanModelName, poolSize, Device.cpu());
        }else {
            faceGanModel.init(modelPath, faceGanModelName, poolSize, Device.gpu());
        }
        return faceGanModel;
    }

    @Bean
    public SrModel srModel() throws IOException, ModelException {
        SrModel srModel = new SrModel();
        if(device.equalsIgnoreCase("cpu")){
            srModel.init(modelPath, srModelName, poolSize, Device.cpu());
        }else {
            srModel.init(modelPath, srModelName, poolSize, Device.gpu());
        }
        return srModel;
    }
}