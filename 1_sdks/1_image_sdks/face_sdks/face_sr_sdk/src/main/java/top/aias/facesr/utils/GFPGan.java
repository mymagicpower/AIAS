package top.aias.facesr.utils;

import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.facesr.translator.GFPTranslator;

import java.nio.file.Paths;
/**
 * 人脸修复
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class GFPGan {

    private static final Logger logger = LoggerFactory.getLogger(GFPGan.class);

    public GFPGan() {
    }

    public Criteria<Image, Image> criteria() {
        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/gfpgan_traced_model.pt"))
                        .optTranslator(new GFPTranslator())
                        .optProgress(new ProgressBar())
//                        .optDevice(Device.cpu())
                        .build();

        return criteria;
    }
}
