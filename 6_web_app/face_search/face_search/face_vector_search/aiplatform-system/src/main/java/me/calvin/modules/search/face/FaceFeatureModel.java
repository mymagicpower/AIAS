package me.calvin.modules.search.face;

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
 * 人脸特征提取
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceFeatureModel implements AutoCloseable {
    private ZooModel<Image, float[]> model;
    private FaceFeaturePool faceFeaturePool;

    public void init(String modelUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelUri));
        this.faceFeaturePool = new FaceFeaturePool(model, poolSize);
    }

    public float[] predict(Image image) throws TranslateException {
        Predictor<Image, float[] > predictor = faceFeaturePool.getPredictor();
        float[] feature = predictor.predict(image);
        faceFeaturePool.releaseDetector(predictor);
        return feature;
    }

    public void close(){
        this.model.close();
        this.faceFeaturePool.close();
    }

    private Criteria<Image, float[]> criteria(String modelUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new FaceFeatureTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("OnnxRuntime")
//            .optDevice(Device.cpu())
                        .build();

        return criteria;
    }

}
