package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.DptDepthModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 深度估计 DPT模型
 * 通过提取原始图片中的深度信息，生成具有原图同样深度结构的深度图，越白的越靠前，越黑的越靠后。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class DptDepthEstimationExample {

    private static final Logger logger = LoggerFactory.getLogger(DptDepthEstimationExample.class);

    private DptDepthEstimationExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/stormtrooper.png");
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/dpt_depth.pt";

        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (DptDepthModel model = new DptDepthModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "dptDepth.png", "build/output");
        }
    }
}
