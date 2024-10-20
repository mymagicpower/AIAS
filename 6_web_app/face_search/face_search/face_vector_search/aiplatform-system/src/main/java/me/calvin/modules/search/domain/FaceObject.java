package me.calvin.modules.search.domain;

import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.Point;
import lombok.Data;

import java.util.List;
/**
 * 人脸对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
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
