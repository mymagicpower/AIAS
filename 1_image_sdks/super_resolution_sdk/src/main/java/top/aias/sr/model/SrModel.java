package top.aias.sr.model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.sr.translator.SrTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 超分辨- 提升分辨率
 * Super Resolution - Enhance Resolution
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class SrModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private SrPool srPool;

    public SrModel(String modelPath, String modelName, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, modelName, poolSize, device);
    }

    public void init(String modelPath, String modelName, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelPath, modelName, device));
        this.srPool = new SrPool(model, poolSize);
    }

    public Image predict(Image img) throws TranslateException {
        Predictor<Image, Image> predictor = srPool.getPredictor();
        Image segImg = predictor.predict(img);
        srPool.releasePredictor(predictor);
        return segImg;
    }

    public void close() {
        this.model.close();
        this.srPool.close();
    }

    private Criteria<Image, Image> criteria(String modelPath, String modelName, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new SrTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
