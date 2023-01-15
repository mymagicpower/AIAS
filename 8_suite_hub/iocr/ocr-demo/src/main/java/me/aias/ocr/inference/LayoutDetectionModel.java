package me.aias.ocr.inference;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.ocr.model.TableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 关于如何优化性能，请参考ocr_sdk中的多线程处理，以及： http://aias.top/AIAS/guides/performance.html
 * @author Calvin
 * @date Oct 20, 2021
 */
public final class LayoutDetectionModel {

    private ZooModel<Image, DetectedObjects> model;

    public void init(String layoutUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(layoutUri));
    }

    public void close() {
        this.model.close();
    }

    public DetectedObjects predict(Image image) throws TranslateException {
        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            return predictor.predict(image);
        }
    }

    private Criteria<Image, DetectedObjects> detectCriteria(String layoutUri) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelUrls(layoutUri)

                        .optTranslator(new LayoutDetectionTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
