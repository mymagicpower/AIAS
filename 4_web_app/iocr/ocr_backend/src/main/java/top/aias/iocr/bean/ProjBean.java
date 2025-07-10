package top.aias.iocr.bean;

import ai.djl.modality.cv.Image;
import lombok.Data;
/**
 * 透视变换对象
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Data
public class ProjBean {
  private Image image;
  private org.opencv.core.Mat warpMat;
}
