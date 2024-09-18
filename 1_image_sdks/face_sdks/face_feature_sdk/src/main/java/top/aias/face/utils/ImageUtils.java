package top.aias.face.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 图片工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class ImageUtils {

  /**
   * 保存图片
   *
   * @param img
   * @param name
   * @param path
   */
  public static void saveImage(Image img, String name, String path) {
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    try {
      img.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 保存图片及检测框
   *
   * @param img
   * @param detection
   * @param name
   * @param path
   * @throws IOException
   */
  public static void saveBoundingBoxImage(
          Image img, DetectedObjects detection, String name, String path) throws IOException {
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    img.save(Files.newOutputStream(imagePath), "png");
  }
}
