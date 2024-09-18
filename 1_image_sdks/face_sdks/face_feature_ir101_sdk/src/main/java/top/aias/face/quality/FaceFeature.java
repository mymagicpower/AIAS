package top.aias.face.quality;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 人脸质量
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class FaceFeature implements AutoCloseable {
    ZooModel model;
    Predictor<Image, float[]> predictor;
    private Device device;

    public FaceFeature(Device device) throws ModelException, IOException {
        this.device = device;
        this.model = ModelZoo.loadModel(onnxCriteria());
        this.predictor = model.newPredictor();
    }

    public float[] predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, float[]> onnxCriteria() {

        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get("models/ir101_traced_model.onnx"))
                        .optTranslator(new FaceFeatureTranslator())
                        .optProgress(new ProgressBar())
                        .optEngine("OnnxRuntime") // Use ONNX engine
                        .build();

        return criteria;
    }
}
