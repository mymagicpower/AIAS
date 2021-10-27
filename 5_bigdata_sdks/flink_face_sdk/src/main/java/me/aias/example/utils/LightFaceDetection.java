/*
 * Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package me.aias.example.utils;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An example of inference using a face detection model.
 *
 * <p>See this <a
 * href="https://github.com/awslabs/djl/blob/master/examples/docs/face_detection.md">doc</a> for
 * information about this example.
 */
public final class LightFaceDetection {

    private static final Logger logger = LoggerFactory.getLogger(LightFaceDetection.class);

    public LightFaceDetection() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path facePath = Paths.get("src/test/resources/faces.jpg");
        Image img = ImageFactory.getInstance().fromFile(facePath);
        Criteria<Image, DetectedObjects> criteria  = new LightFaceDetection().criteria();
        try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria)) {
            try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
                DetectedObjects detections = predictor.predict(img);
                logger.info("{}", detections);
            }
        }
    }

    public Criteria<Image, DetectedObjects> criteria() {
        double confThresh = 0.85f;
        double nmsThresh = 0.45f;
        double[] variance = {0.1f, 0.2f};
        int topK = 20;
        int[][] scales = {{10, 16, 24}, {32, 48}, {64, 96}, {128, 192, 256}};
        int[] steps = {8, 16, 32, 64};

        FaceDetectionTranslator translator =
                new FaceDetectionTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelUrls("https://resources.djl.ai/test-models/pytorch/ultranet.zip")
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .build();

        return criteria;
    }
}
