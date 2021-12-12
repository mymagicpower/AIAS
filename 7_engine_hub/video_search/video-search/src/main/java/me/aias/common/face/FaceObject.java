package me.aias.common.face;

import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.Point;
import lombok.Data;

import java.util.List;

@Data
public class FaceObject {
  private float score;
  private BoundingBox boundingBox;
  private List<Point> keyPoints;
  private List<Float> feature;

  public FaceObject() {
  }

  public FaceObject(float score, BoundingBox box, List<Point> keyPoints) {
    this.score = score;
    this.boundingBox = box;
    this.keyPoints = keyPoints;
  }
}
