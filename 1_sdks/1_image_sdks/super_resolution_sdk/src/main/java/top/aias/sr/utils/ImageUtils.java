package top.aias.sr.utils;

import ai.djl.modality.cv.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 图片工具类
 *
 * @author Calvin
 * @email 179209347@qq.com
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
}
