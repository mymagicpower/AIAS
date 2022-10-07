package me.aias.example.utils.detection;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public final class OcrV3Detection {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3Detection.class);

    public OcrV3Detection() {
    }

    public Criteria<Image, NDList> detectCriteria() {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, NDList.class)
                        .optModelUrls(
                                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv3_det_infer.zip")
                        //            .optModelUrls(
                        // "/Users/calvin/Documents/build/paddle_models/ppocr/ch_PP-OCRv2_det_infer")
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
