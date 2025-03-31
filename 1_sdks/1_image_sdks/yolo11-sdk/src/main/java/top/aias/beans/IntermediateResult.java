package top.aias.beans;

import ai.djl.modality.cv.output.Joints;
import ai.djl.modality.cv.output.Rectangle;
import lombok.Data;

@Data
public class IntermediateResult {
    private double confidence;
    private int id;
    private String detectedClass;
    private Rectangle location;
    private Joints joints;

    public IntermediateResult(int id, double confidence, String detectedClass, Rectangle location) {
        this.id = id;
        this.confidence = confidence;
        this.detectedClass = detectedClass;
        this.location = location;
    }

    public IntermediateResult(double confidence, Rectangle location, Joints joints) {
      this.confidence = confidence;
      this.location = location;
      this.joints = joints;
    }

    public Rectangle getLocation() {
      return new Rectangle(
          location.getX(), location.getY(), location.getWidth(), location.getHeight());
    }
  }