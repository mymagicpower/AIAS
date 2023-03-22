package me.aias.common.utils.opencv;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * 图片类型转换
 * Image type conversion
 *
 * @author Calvin
 */
public class OpenCVImageUtil {

  /**
   * 将 BufferedImage 转 Mat
   * Convert BufferedImage to Mat
   *
   * @param original
   */
  public static Mat bufferedImage2Mat(BufferedImage original) {
    OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
    return cv.convertToMat(new Java2DFrameConverter().convert(original));
  }

  /**
   * 将mat转BufferedImage
   * Convert Mat to BufferedImage
   *
   * @param matrix
   */
  public static BufferedImage mat2BufferedImage(Mat matrix) {
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
    //    BufferedImage对象中最重要的两个组件为Raster和ColorModel，分别用于存储图像的像素数据与颜色数据。
    //    表示像素矩形数组的类。Raster 封装存储样本值的 DataBuffer，以及描述如何在 DataBuffer 中定位给定样本值的 SampleModel。
    //    由于Raster对象是BufferedImage对象中的像素数据存储对象，因此，BufferedImage支持从Raster对象中获取任意位置（x，y）点的像素值p（x，y）。
    // The two most important components of the BufferedImage object are Raster and ColorModel, which are used to store pixel data and color data of the image, respectively.
    // The Raster class represents an array of pixel rectangles. Raster encapsulates the DataBuffer that stores the sample values, and the SampleModel that describes how to locate a given sample value in the DataBuffer.
    // Since the Raster object is the pixel data storage object in the BufferedImage object, BufferedImage supports getting the pixel value p(x,y) at any position (x,y) from the Raster object.
    image.getRaster().setDataElements(0, 0, cols, rows, data);
    return image;
  }

  /**
   * 将bufferImage转Mat
   * Convert bufferImage to Mat
   *
   * @param original
   * @param matType
   * @param msg
   * @param x
   * @param y
   */
  public static Mat bufferedImage2Mat(
          BufferedImage original, int matType, String msg, int x, int y) {
    Graphics2D g = original.createGraphics();
    try {
      g.setComposite(AlphaComposite.Src);
      g.drawImage(original, 0, 0, null);
      g.drawString(msg, x, y);
    } finally {
      g.dispose();
    }
    Mat mat = new Mat(original.getHeight(), original.getWidth(), matType);
    mat.data().put(((DataBufferByte) original.getRaster().getDataBuffer()).getData());
    return mat;
  }
}
