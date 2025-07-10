package top.aias.model;

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
 * 图像编码模型
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class ImageEncoderModel implements AutoCloseable {
    private ZooModel<Image, float[]> model;
    private ImageEncoderPool encoderPool;

    public void init(String modelUri, int poolSize) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(ptCriteria(modelUri));
        this.encoderPool = new ImageEncoderPool(model, poolSize);
    }

    public ZooModel<Image, float[]> getModel() {
        return model;
    }

    public float[] predict(Image image) throws TranslateException {
        Predictor<Image, float[]> predictor = encoderPool.getPredictor();
        float[] feature = predictor.predict(image);
        encoderPool.releasePredictor(predictor);
        return feature;
    }

    public void close(){
        this.model.close();
        this.encoderPool.close();
    }

    private Criteria<Image, float[]> ptCriteria(String modelUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelPath(Paths.get(modelUri))
                        .optTranslator(new ImageTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
