package top.aias.sd.preprocess.tests;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.preprocess.lineart.MlsdModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * MLSD 线条检测
 * MLSD 线条检测用于生成房间、直线条的建筑场景效果比较好。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class MlsdExample {

    private static final Logger logger = LoggerFactory.getLogger(MlsdExample.class);

    private MlsdExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/mlsd.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "models/mlsd.pt";

        try (MlsdModel model = new MlsdModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "mlsd_pt.png", "build/output");
        }
    }
}
