package me.aias.common.face;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 20, 2021
 */
public final class FaceDetectionModel {
    private ZooModel<Image, DetectedObjects> model;

    public void init(String layoutUri, int topK, double confThresh, double nmsThresh) throws MalformedModelException, ModelNotFoundException, IOException {
        this.model = ModelZoo.loadModel(detectCriteria(layoutUri, topK, confThresh, nmsThresh));
    }

    public ZooModel<Image, DetectedObjects> getModel() {
        return model;
    }

    public void close() {
        this.model.close();
    }

    private Criteria<Image, DetectedObjects> detectCriteria(String layoutUri, int topK, double confThresh, double nmsThresh) {
        double[] variance = {0.1f, 0.2f};

        int[][] scales = {{10, 16, 24}, {32, 48}, {64, 96}, {128, 192, 256}};
        int[] steps = {8, 16, 32, 64};

        FaceDetectionTranslator translator =
                new FaceDetectionTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelUrls(layoutUri)
                        .optTranslator(translator)
                        .optEngine("PyTorch") // Use PyTorch engine
                        .optDevice(Device.cpu())
                        .optProgress(new ProgressBar())
                        .build();
        return criteria;
    }

}
