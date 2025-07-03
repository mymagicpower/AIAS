package me.calvin.modules.search.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 图片特征提取模型
 * Image Feature Extraction Model
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class ImageEncoderModel implements AutoCloseable {
    private ZooModel<Image, float[]> model;
    private ImageFeaturePool imageFeaturePool;

    public void init(String modelUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelUri));
        this.imageFeaturePool = new ImageFeaturePool(model, poolSize);
    }

    public ZooModel<Image, float[]> getModel() {
        return model;
    }

    public float[] predict(Image image) throws TranslateException {
        Predictor<Image, float[]> predictor = imageFeaturePool.getPredictor();
        float[] feature = predictor.predict(image);
        imageFeaturePool.releaseDetector(predictor);
        return feature;
    }

    public void close() {
        this.model.close();
        this.imageFeaturePool.close();
    }

    private Criteria<Image, float[]> criteria(String modelUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new ImageTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .build();

        return criteria;
    }

}
