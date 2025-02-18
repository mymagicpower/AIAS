package top.aias.platform.configuration;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.aias.platform.model.mlsd.MlsdSquareModel;
import top.aias.platform.model.ocr.RecognitionModel;

import java.io.File;

/**
 * 模型配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Component
public class ModelConfigOcr {
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

    @Bean
    public RecognitionModel recognitionModel() {
//        Device device;
//        if (deviceType.equalsIgnoreCase("cpu")) {
//            device = Device.cpu();
//        } else {
//            device = Device.gpu();
//        }

        // 拼接路径
        String ocrDet = modelPath + "ocr" + File.separator + "ch_PP-OCRv4_det_infer.zip";
        String ocrRec = modelPath + "ocr" + File.separator + "ch_PP-OCRv4_rec_infer.zip";

        // 模型只支持CPU，需要 GPU 导出新模型
        RecognitionModel recognitionModel = new RecognitionModel(ocrDet, ocrRec, poolSize, Device.cpu());

        if (loadMode.equalsIgnoreCase("eager")) {
            recognitionModel.ensureInitialized();
        }
        
        return recognitionModel;
    }

    @Bean
    public MlsdSquareModel mlsdSquareModel() {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        // 拼接路径
        String mlsd = modelPath + "ocr" + File.separator + "mlsd_traced_model_onnx.zip";

        MlsdSquareModel mlsdSquareModel = new MlsdSquareModel(mlsd, poolSize, device);

        if (loadMode.equalsIgnoreCase("eager")) {
            mlsdSquareModel.ensureInitialized();
        }
        return mlsdSquareModel;
    }
}