package me.calvin.modules.search.domain;

import lombok.Data;

import java.util.List;
/**
 * 简单人脸信息对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Data
public class SimpleFaceObject {
  private int id;
  private float score;
  private int x;
  private int y;
  private int width;
  private int height;
  private List<Float> feature;
}
