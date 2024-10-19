package top.aias.img.model;

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
import top.aias.img.model.pool.SegPool;
import top.aias.img.translator.IsNetTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * isnet-general-use ：一个新的通用的预训练模型。
 * isnet-anime：专门针对动画人物的高精度分割。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class IsNetModel implements AutoCloseable {
    private ZooModel<Image, Image> model;
    private SegPool segPool;

    public IsNetModel(){}

    public IsNetModel(String modelPath, String modelName, int poolSize, boolean mask, Device device) throws ModelException, IOException {
        init(modelPath, modelName, poolSize, mask, device);
    }

    public void init(String modelPath, String modelName, int poolSize, boolean mask, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelPath, modelName, mask, device));
        this.segPool = new SegPool(model, poolSize);
    }

    public Image predict(Image img) throws TranslateException {
        Predictor<Image, Image> predictor = segPool.getPredictor();
        Image segImg = predictor.predict(img);
        segPool.releasePredictor(predictor);
        return segImg;
    }

    public void close() {
        this.model.close();
        this.segPool.close();
    }

    private Criteria<Image, Image> criteria(String modelPath, String modelName, boolean mask, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new IsNetTranslator(mask))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
