package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.ocr.utils.ImageUtils;
import top.aias.ocr.utils.TextSrOnnx;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 超分辨- 提升图片文本分辨率(支持 CPU/GPU)
 * Super Resolution - Enhance 4x Resolution
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class TextSrOnnxExample {

    private static final Logger logger = LoggerFactory.getLogger(TextSrOnnxExample.class);

    private TextSrOnnxExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String imagePath = "src/test/resources/";
        Path imageFile = Paths.get(imagePath + "t4.jpg");

        Image image = ImageFactory.getInstance().fromFile(imageFile);

        // 单行文本图片超分 detectResolution 可以设置为 32（参考值，具体根据测试效果修改）
        // 整张图片超分 detectResolution 根据图片实际情况设置。
        try (TextSrOnnx textSrOnnx = new TextSrOnnx(256, Device.cpu());) {

            long start = System.currentTimeMillis();

            Image img = textSrOnnx.predict(image);

            long end = System.currentTimeMillis();
            System.out.println("time: "+ (end - start));

            ImageUtils.saveImage(img, "text-bsr-onnx.png", "build/output");
        }


    }
}
