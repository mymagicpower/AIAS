package top.aias.sd.preprocess.tests;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.preprocess.edge.HedModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 边缘检测 hed模型，safe模式
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class HedExample {

    private static final Logger logger = LoggerFactory.getLogger(HedExample.class);

    private HedExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/softedge.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "models/hed.pt";

        try (HedModel model = new HedModel(512, 512, modelPath, false, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "hed_pt.png", "build/output");
        }
    }
}
