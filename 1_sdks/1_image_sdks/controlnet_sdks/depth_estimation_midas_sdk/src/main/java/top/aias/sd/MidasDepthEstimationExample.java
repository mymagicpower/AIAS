package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sd.controlnet.MidasDepthModel;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 深度估计
 * 通过提取原始图片中的深度信息，生成具有原图同样深度结构的深度图，越白的越靠前，越黑的越靠后。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class MidasDepthEstimationExample {

    private static final Logger logger = LoggerFactory.getLogger(MidasDepthEstimationExample.class);

    private MidasDepthEstimationExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/toy.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/midas_depth.pt";

        try (MidasDepthModel model = new MidasDepthModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "midasDepth.png", "build/output");
        }
    }
}
