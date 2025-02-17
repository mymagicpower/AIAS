package top.aias.sd.preprocess.tests;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.preprocess.normal.NormalBaeModel;
import top.aias.sd.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Normal Map 法线贴图
 * 根据图片生成法线贴图，适合CG或游戏美术师。法线贴图能根据原始素材生成一张记录凹凸信息的法线贴图，便于AI给图片内容进行更好的光影处理，
 * 它比深度模型对于细节的保留更加的精确。法线贴图在游戏制作领域用的较多，常用于贴在低模上模拟高模的复杂光影效果。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class NormalBaeExample {

    private static final Logger logger = LoggerFactory.getLogger(NormalBaeExample.class);

    private NormalBaeExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/toy.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String modelPath = "models/normalbae.pt";

        try (NormalBaeModel model = new NormalBaeModel(512, 512, modelPath, 1, Device.cpu())) {
            Image depthImg = model.predict(img);
            ImageUtils.saveImage(depthImg, "normalbae_pt.png", "build/output");
        }
    }
}
