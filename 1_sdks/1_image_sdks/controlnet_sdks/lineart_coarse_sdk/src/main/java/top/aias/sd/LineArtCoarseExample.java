package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.LineArtCoarseModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Lineart 生成线稿，边缘粗糙处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class LineArtCoarseExample {

    private static final Logger logger = LoggerFactory.getLogger(LineArtCoarseExample.class);

    private LineArtCoarseExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/lineart.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/lineart_coarse.pt";

        try (LineArtCoarseModel model = new LineArtCoarseModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "lineArtCoarse_pt.png", "build/output");
        }
    }
}
