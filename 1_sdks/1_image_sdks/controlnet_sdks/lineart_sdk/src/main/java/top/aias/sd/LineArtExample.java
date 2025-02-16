package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.controlnet.LineArtModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Lineart 生成线稿
 * Lineart 边缘检测预处理器可很好识别出图像内各对象的边缘轮廓，用于生成线稿。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class LineArtExample {

    private static final Logger logger = LoggerFactory.getLogger(LineArtExample.class);

    private LineArtExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/fullbody.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "/Users/calvin/AIAS/3_api_platform/api-platform/models/controlnet/lineart.pt";

        try (LineArtModel model = new LineArtModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "lineArt_pt.png", "build/output");
        }
    }
}
