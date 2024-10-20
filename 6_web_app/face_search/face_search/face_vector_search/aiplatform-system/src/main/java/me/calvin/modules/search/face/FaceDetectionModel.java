package me.calvin.modules.search.face;

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

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 人脸检测
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class FaceDetectionModel implements AutoCloseable {
    ZooModel model;
    private FaceDetPool faceDetPool;

    public void init(String modelUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(mobileOnnxCriteria(modelUri));
        this.faceDetPool = new FaceDetPool(model, poolSize);
    }

    public DetectedObjects predict(Image image) throws TranslateException {
        Predictor<Image, DetectedObjects> predictor = faceDetPool.getPredictor();
        DetectedObjects detectedObjects = predictor.predict(image);
        faceDetPool.releaseDetector(predictor);
        return detectedObjects;
    }

    public void close(){
        this.model.close();
        this.faceDetPool.close();
    }

    /**
     * Retinanet ONNX 大模型，精度高，速度略慢
     * @param modelUri
     * @return
     */
    private Criteria<Image, DetectedObjects> retinaOnnxCriteria(String modelUri) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new FaceDetTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("OnnxRuntime") // Use ONNX engine
                        .build();

        return criteria;
    }

    /**
     * Mobilenet ONNX 小模型，精度略低，速度更快
     * @param modelUri
     * @return
     */
    private Criteria<Image, DetectedObjects> mobileOnnxCriteria(String modelUri) {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new MobileFaceDetTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("OnnxRuntime") // Use ONNX engine
                        .build();

        return criteria;
    }
}
