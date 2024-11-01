package top.aias.ocr.model;

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
import top.aias.ocr.model.pool.LayoutPool;
import top.aias.ocr.translator.PicoDetLayoutDetectionTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 布局检测模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class LayoutDetectionModel implements AutoCloseable {

    private ZooModel<Image, DetectedObjects> model;
    private LayoutPool layoutPool;

    public void init(String layoutUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(layoutUri));
        this.layoutPool = new LayoutPool(poolSize, model);
    }

    public void close() {
        this.model.close();
        this.layoutPool.close();
    }

    public DetectedObjects predict(Image image) throws TranslateException {
        Predictor<Image, DetectedObjects> predictor = layoutPool.getPredictor();
        DetectedObjects detectedObjects = predictor.predict(image);
        layoutPool.releasePredictor(predictor);
        return detectedObjects;
    }

    private Criteria<Image, DetectedObjects> criteria(String model) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(model))
//                        .optModelUrls(model)
                        .optTranslator(new PicoDetLayoutDetectionTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
