/*
 * Copyright (c) 2020-2021 CertifAI Sdn. Bhd.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package top.aias.dl4j.examples.detection.dataHelpers;

import org.deeplearning4j.nn.layers.objdetect.DetectedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created on Jun 21, 2018 , 4:26:26 PM
 *
 * @author Taha Emara
 * Email : taha@emaraic.com
 * Website: http://www.emaraic.com
 * Implementation is based on code from here https://deeplearning4j.org/autonomous-vehicle-java
 */
public class NonMaxSuppression {

    private static void removeObjectsIntersectingWithMax(ArrayList<DetectedObject> detectedObjects, DetectedObject maxObjectDetect) {
        double[] bottomRightXY1 = maxObjectDetect.getBottomRightXY();
        double[] topLeftXY1 = maxObjectDetect.getTopLeftXY();
        List<DetectedObject> removeIntersectingObjects = new ArrayList<>();
        for (DetectedObject detectedObject : detectedObjects) {
            double[] topLeftXY = detectedObject.getTopLeftXY();
            double[] bottomRightXY = detectedObject.getBottomRightXY();
            double iox1 = Math.max(topLeftXY[0], topLeftXY1[0]);
            double ioy1 = Math.max(topLeftXY[1], topLeftXY1[1]);

            double iox2 = Math.min(bottomRightXY[0], bottomRightXY1[0]);
            double ioy2 = Math.min(bottomRightXY[1], bottomRightXY1[1]);

            double inter_area = (ioy2 - ioy1) * (iox2 - iox1);

            double box1_area = (bottomRightXY1[1] - topLeftXY1[1]) * (bottomRightXY1[0] - topLeftXY1[0]);
            double box2_area = (bottomRightXY[1] - topLeftXY[1]) * (bottomRightXY[0] - topLeftXY[0]);

            double union_area = box1_area + box2_area - inter_area;
            double iou = inter_area / union_area;

            if (iou > 0.4) {
                removeIntersectingObjects.add(detectedObject);
            }
        }
        detectedObjects.removeAll(removeIntersectingObjects);
    }

    public static List<DetectedObject> getObjects(List<DetectedObject> predictedObjects) {
        ArrayList<DetectedObject> objects = new ArrayList<>();
        if (predictedObjects == null) {
            return predictedObjects;
        }
        ArrayList<DetectedObject> detectedObjects = new ArrayList<>(predictedObjects);
        while (!detectedObjects.isEmpty()) {
            Optional<DetectedObject> max = detectedObjects.stream().max((o1, o2) -> ((Double) o1.getConfidence()).compareTo(o2.getConfidence()));
            if (max.isPresent()) {
                DetectedObject maxObjectDetect = max.get();
                removeObjectsIntersectingWithMax(detectedObjects, maxObjectDetect);
                detectedObjects.remove(maxObjectDetect);
                objects.add(maxObjectDetect);
            }
        }
        return objects;
    }
}
