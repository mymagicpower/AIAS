package me.aias.ocr.model;

import lombok.Data;

import java.util.List;

@Data
public class LabelBean {
  private int index;
  private int active;
  private String type;
  private String value;
  private String field;
  private List<Point> points;
  private ai.djl.modality.cv.output.Point centerPoint;
}
