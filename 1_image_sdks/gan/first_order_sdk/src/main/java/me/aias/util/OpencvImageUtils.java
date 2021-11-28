package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Opencv图像处理
 *
 * @author Calvin
 */
public class OpencvImageUtils {
  /**
   * 将mat转BufferedImage
   *
   * @param matrix
   */
  public static BufferedImage m2B(Mat matrix) {
    int cols = matrix.cols();
    int rows = matrix.rows();
    int elemSize = (int) matrix.elemSize();
    byte[] data = new byte[cols * rows * elemSize];

    matrix.data().get(data);

    int type = 0;
    switch (matrix.channels()) {
      case 1:
        type = BufferedImage.TYPE_BYTE_GRAY;
        break;
      case 3:
        type = BufferedImage.TYPE_3BYTE_BGR;
        byte b;
        for (int i = 0; i < data.length; i = i + 3) {
          b = data[i];
          data[i] = data[i + 2];
          data[i + 2] = b;
        }
        break;
      default:
        return null;
    }
    BufferedImage image = new BufferedImage(cols, rows, type);
    image.getRaster().setDataElements(0, 0, cols, rows, data);
    return image;
  }

  /**
   * 将BufferedImage转mat
   *
   * @param original
   * @param matType
   */
  public static Mat b2M(BufferedImage original, int matType) {
    original = convert(original, BufferedImage.TYPE_3BYTE_BGR);
    Mat mat = new Mat(original.getHeight(), original.getWidth(), matType);
    mat.data().put(((DataBufferByte) original.getRaster().getDataBuffer()).getData());
    return mat;
  }

  /**
   * 将BufferedImage类型转换
   *
   * @param src
   * @param bufImgType
   */
  public static BufferedImage convert(BufferedImage src, int bufImgType) {
    BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), bufImgType);
    Graphics2D g2d = img.createGraphics();
    g2d.drawImage(src, 0, 0, null);
    g2d.dispose();
    return img;
  }
}
