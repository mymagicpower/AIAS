package top.aias.facesr.model;

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
import top.aias.facesr.translator.DdcolorTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *  照片上色
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class DdcolorModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private DdcolorPool srPool;

    public DdcolorModel(String modelPath, String modelName, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, modelName, poolSize, device);
    }

    public void init(String modelPath, String modelName, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelPath, modelName, device));
        this.srPool = new DdcolorPool(model, poolSize);
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
                        .optTranslator(new DdcolorTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
