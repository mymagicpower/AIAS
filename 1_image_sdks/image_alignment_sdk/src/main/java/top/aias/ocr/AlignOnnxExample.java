package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.align.MlsdSquareOnnx;
import top.aias.ocr.utils.common.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 文本转正使用例子（onnx模型）
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class AlignOnnxExample {

    private static final Logger logger = LoggerFactory.getLogger(AlignOnnxExample.class);

    private AlignOnnxExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/warp1.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (MlsdSquareOnnx mlsdSquareOnnx = new MlsdSquareOnnx(Device.cpu(),false);) {

            Image cropImg = mlsdSquareOnnx.predict(img);
            if (cropImg != null)
                ImageUtils.saveImage(cropImg, "mlsdSquare.png", "build/output");
            else
                System.out.println("Square not found");
        }
    }
}
