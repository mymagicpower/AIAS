package top.aias.sr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sr.model.SrModel;
import top.aias.sr.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图像高清
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class HDExample {

    private static final Logger logger = LoggerFactory.getLogger(HDExample.class);

    private HDExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/srgan.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SrModel srModel = new SrModel("models/", "realsr_traced_model.pt", 1, Device.cpu())) {
            if(image.getHeight()> 512 && image.getHeight() > 512){
                image = image.resize(image.getWidth() / 4, image.getHeight() / 4, true);
                Image img = srModel.predict(image);
                ImageUtils.saveImage(img, "hd.png", "build/output");
            }else {
                Image img = srModel.predict(image);
                img = img.resize(img.getWidth() / 4, img.getHeight() / 4, true);
                ImageUtils.saveImage(img, "hd.png", "build/output");
            }



        }
    }
}
