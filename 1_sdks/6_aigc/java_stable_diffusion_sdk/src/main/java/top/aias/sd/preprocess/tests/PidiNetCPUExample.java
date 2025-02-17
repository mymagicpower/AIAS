package top.aias.sd.preprocess.tests;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sd.preprocess.edge.PidiNetModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 边缘检测 PidiNet 模型，safe模式
 * SoftEdge 边缘检测可保留更多柔和的边缘细节，类似手绘效果。
 * https://huggingface.co/lllyasviel/control_v11p_sd15_softedge
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class PidiNetCPUExample {

    private static final Logger logger = LoggerFactory.getLogger(PidiNetCPUExample.class);

    private PidiNetCPUExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/pose.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "models/pidi_cpu.pt";

        try (PidiNetModel model = new PidiNetModel(512, 512, modelPath, false, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "pidiNet_cpu.png", "build/output");
        }
    }
}
