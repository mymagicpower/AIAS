package me.aias.example.utils;

import ai.djl.modality.cv.output.BoundingBox;

import java.util.List;

public class TableResult {
  private List<String> structure_str_list;
  private List<BoundingBox> boxes;

  public TableResult(List<String> structure_str_list, List<BoundingBox> boxes) {
    this.structure_str_list = structure_str_list;
    this.boxes = boxes;
  }

  public List<String> getStructure_str_list() {
    return structure_str_list;
  }

  public void setStructure_str_list(List<String> structure_str_list) {
    this.structure_str_list = structure_str_list;
  }

  public List<BoundingBox> getBoxes() {
    return boxes;
  }

  public void setBoxes(List<BoundingBox> boxes) {
    this.boxes = boxes;
  }
}
