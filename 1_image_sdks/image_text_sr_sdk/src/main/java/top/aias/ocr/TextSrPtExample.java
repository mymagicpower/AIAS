package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.ocr.utils.ImageUtils;
import top.aias.ocr.utils.TextSrPt;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 超分辨- 提升文本图片分辨率(支持 CPU/GPU)
 * Super Resolution - Enhance 4x Resolution
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class TextSrPtExample {

    private static final Logger logger = LoggerFactory.getLogger(TextSrPtExample.class);

    private TextSrPtExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String imagePath = "src/test/resources/";
        Path imageFile = Paths.get(imagePath + "t2.png");

        Image image = ImageFactory.getInstance().fromFile(imageFile);

        try (TextSrPt textSrPt = new TextSrPt(32, Device.cpu());) {

            long start = System.currentTimeMillis();

            Image img = textSrPt.predict(image);

            long end = System.currentTimeMillis();
            System.out.println("time: " + (end - start));

            ImageUtils.saveImage(img, "text-bsr-pt.png", "build/output");
        }


    }
}
