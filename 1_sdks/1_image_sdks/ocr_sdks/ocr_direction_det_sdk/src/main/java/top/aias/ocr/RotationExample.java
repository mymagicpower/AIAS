package top.aias.ocr;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import top.aias.ocr.utils.common.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图片旋转
 * Rotation Example
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class RotationExample {

    private static final Logger logger = LoggerFactory.getLogger(RotationExample.class);

    private RotationExample() {
    }

    public static void main(String[] args) throws IOException {
        Path imageFile = Paths.get("src/test/resources/ticket_0.png");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        // 逆时针旋转
        // Counterclockwise rotation
        image = ImageUtils.rotateImg(image,1);

        ImageUtils.saveImage(image, "rotated_result.png", "build/output");
    }

}
