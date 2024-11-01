package top.aias.ocr.bean;

import lombok.Data;

import java.util.List;
/**
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
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
