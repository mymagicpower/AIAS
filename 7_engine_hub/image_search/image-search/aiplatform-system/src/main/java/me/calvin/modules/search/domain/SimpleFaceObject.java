package me.calvin.modules.search.domain;

import lombok.Data;

import java.util.List;

@Data
public class SimpleFaceObject {
  private float score;
  private int x;
  private int y;
  private int width;
  private int height;
  private List<Float> feature;
}
