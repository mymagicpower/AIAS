package top.aias.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.face.quality.FaceFeature;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 人脸特征, 支持 CPU & GPU
 * 输入经过对齐的人脸图片(112x112)，返回人脸特征向量(512维)
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class FaceFeatureExample {
    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path path = Paths.get("src/test/resources/face_align_0.png");
        Image image = new OpenCVImageFactory().fromFile(path);

        try (FaceFeature faceFeature = new FaceFeature(Device.gpu())) {
            float[] result = faceFeature.predict(image);
            System.out.println(Arrays.toString(result));
        }
    }
}
