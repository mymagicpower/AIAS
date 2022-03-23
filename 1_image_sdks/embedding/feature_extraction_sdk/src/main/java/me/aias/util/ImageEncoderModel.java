package me.aias.util;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import java.io.IOException;

/**
 * 图片特征提取模型
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public final class ImageEncoderModel {
    private ZooModel<Image, float[]> model;

    public Criteria<Image, float[]> criteria() {
        Criteria<Image, float[]> criteria =
                Criteria.builder()
                        .setTypes(Image.class, float[].class)
                        .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/clip_series/CLIP-ViT-B-32-IMAGE.zip")
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
