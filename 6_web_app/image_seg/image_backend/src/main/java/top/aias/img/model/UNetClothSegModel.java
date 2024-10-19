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
import top.aias.img.translator.ClothSegTranslator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * u2net_cloth_seg：专门从人像上抠衣服的预训练模型，它会把衣服分成三部分：上半身、下半身和全身。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class UNetClothSegModel implements AutoCloseable {
    ZooModel model;
    private SegPool segPool;

    public UNetClothSegModel(){}
    public UNetClothSegModel(String modelPath, String modelName, int clothCategory, int poolSize, Device device) throws ModelException, IOException {
        init(modelPath, modelName, clothCategory, poolSize, device);
    }

    public void init(String modelPath, String modelName, int clothCategory, int poolSize, Device device) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(criteria(modelPath, modelName, clothCategory, device));
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

    private Criteria<Image, Image> criteria(String modelPath, String modelName, int clothCategory, Device device) {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath + modelName))
                        .optDevice(device)
                        .optTranslator(new ClothSegTranslator(clothCategory))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
