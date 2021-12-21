package me.aias.model;

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

/**
 * 图片特征提取模型
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public final class ImageEncoderModel {

    private ZooModel<Image, float[]> model;
    private Predictor<Image, float[]> predictor;

    public void init(String modelUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(modelUri));
        this.predictor = model.newPredictor();
    }

    public void close() {
        this.model.close();
        this.predictor.close();
    }

    public float[] predict(Image image) throws TranslateException {
        return predictor.predict(image);
    }

    private Criteria<Image, float[]> detectCriteria(String modelUri) {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelUrls(modelUri)
                        //.optModelUrls("/Users/calvin/CLIP-ViT-B-32-IMAGE/CLIP-ViT-B-32-IMAGE.zip")
                        .optTranslator(new ImageTranslator())
                        .optEngine("PyTorch") // Use PyTorch engine
                        // This model was traced on CPU and can only run on CPU
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

}
