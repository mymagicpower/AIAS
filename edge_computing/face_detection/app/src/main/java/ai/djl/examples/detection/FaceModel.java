/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package ai.djl.examples.detection;

import java.io.IOException;

import ai.djl.ModelException;
import ai.djl.examples.detection.domain.FaceDetectedObjects;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;

final class FaceModel {

    private FaceModel() {
    }

    public static ZooModel<Image, FaceDetectedObjects> loadModel() throws ModelException, IOException {
        double confThresh = 0.85f;
        double nmsThresh = 0.45f;
        double[] variance = {0.1f, 0.2f};
        int topK = 5000;
        int[][] scales = {{10, 16, 24}, {32, 48}, {64, 96}, {128, 192, 256}};
        int[] steps = {8, 16, 32, 64};

        FaceDetectionTranslator translator =
                new FaceDetectionTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

        Criteria<Image, FaceDetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, FaceDetectedObjects.class)
                        .optModelUrls("https://djl-model.oss-cn-hongkong.aliyuncs.com/ultranet.zip")
                        // https://resources.djl.ai/test-models/pytorch/ultranet.zip
                        // https://djl-model.oss-cn-hongkong.aliyuncs.com/ultranet.zip
                        .optTranslator(translator)
                        .build();

        return ModelZoo.loadModel(criteria);
    }
}
