package top.aias.beans;

import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Joints;
import ai.djl.modality.cv.output.Rectangle;
import lombok.Data;

import java.util.List;

@Data
public class KeyPointsDetResult {
    private DetectedObjects detectedObjects;
    private List<Joints> jointsList;

    public KeyPointsDetResult(DetectedObjects detectedObjects, List<Joints> jointsList) {
        this.detectedObjects = detectedObjects;
        this.jointsList = jointsList;
    }
  }