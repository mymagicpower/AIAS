package me.aias.example.utils.common;

import ai.djl.modality.cv.Image;
import lombok.Data;

import java.util.List;

@Data
public class ProjItemBean {
  private Image image;
  private org.opencv.core.Mat warpMat;
}
