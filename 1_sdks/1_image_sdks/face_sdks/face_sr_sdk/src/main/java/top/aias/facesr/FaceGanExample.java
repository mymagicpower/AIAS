package top.aias.facesr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.facesr.model.FaceModel;
import top.aias.facesr.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人脸超分
 * Face Super Resolution
 *
 * @author Calvin
 * @email 179209347@qq.com
 **/
public final class FaceGanExample {

    private static final Logger logger = LoggerFactory.getLogger(FaceGanExample.class);

    private FaceGanExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/face_align_0.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (FaceModel srModel = new FaceModel("models/", "gfpgan_traced_model.pt", 1, Device.cpu())) {
            Image img = srModel.predict(image);
            ImageUtils.saveImage(img, "gfpGan.png", "build/output");
        }
    }
}
