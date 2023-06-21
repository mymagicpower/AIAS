package me.aias.example.detection;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Paths;

public final class OcrV3Detection {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3Detection.class);

    public OcrV3Detection() {
    }

    public Criteria<Image, DetectedObjects> detectCriteria() {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_det_infer_onnx.zip"))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
