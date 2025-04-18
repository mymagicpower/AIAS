package top.aias.domain;

import lombok.Data;

import java.util.List;
/**
 * 人脸对象类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Data
public class SimpleFaceObject {
  private float score;
  private int x;
  private int y;
  private int width;
  private int height;
  private List<Float> feature;
}
