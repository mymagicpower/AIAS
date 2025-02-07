package top.aias.face.util;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;

import java.awt.image.BufferedImage;
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
  public static void saveImage(BufferedImage img, String name, String path) {
    ai.djl.modality.cv.Image djlImg = ImageFactory.getInstance().fromImage(img);
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    try {
      djlImg.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
   * 保存图片，含检测框
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

  /**
   * 返回外扩人脸 factor = 1, 100%, factor = 0.2, 20%
   * Returns the enlarged face with factor = 1, 100%, factor = 0.2, 20%
   *
   * @param img
   * @param box
   * @param factor
   * @return
   */
  public static Image getSubImage(Image img, BoundingBox box, float factor) {
    Rectangle rect = box.getBounds();
    // 左上角坐标 - Upper left corner coordinates
    int x1 = (int) (rect.getX() * img.getWidth());
    int y1 = (int) (rect.getY() * img.getHeight());
    // 宽度，高度 - width, height
    int w = (int) (rect.getWidth() * img.getWidth());
    int h = (int) (rect.getHeight() * img.getHeight());
    // 左上角坐标 - Upper right corner coordinates
    int x2 = x1 + w;
    int y2 = y1 + h;

    // 外扩大100%，防止对齐后人脸出现黑边
    // Expand by 100% to prevent black edges after alignment
    int new_x1 = Math.max((int) (x1 + x1 * factor / 2 - x2 * factor / 2), 0);
    int new_x2 = Math.min((int) (x2 + x2 * factor / 2 - x1 * factor / 2), img.getWidth() - 1);
    int new_y1 = Math.max((int) (y1 + y1 * factor / 2 - y2 * factor / 2), 0);
    int new_y2 = Math.min((int) (y2 + y2 * factor / 2 - y1 * factor / 2), img.getHeight() - 1);
    int new_w = new_x2 - new_x1;
    int new_h = new_y2 - new_y1;

    return img.getSubImage(new_x1, new_y1, new_w, new_h);
  }

  /**
   * 获取左上角x坐标 - Upper left corner x coordinate
   *
   * @param img
   * @param box
   * @return
   */
  public static int getX(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    // 左上角x坐标 - Upper left corner x coordinate
    int x = (int) (rect.getX() * img.getWidth());
    return x;
  }

  /**
   * 获取左上角y坐标 - Upper left corner y coordinate
   *
   * @param img
   * @param box
   * @return
   */
  public static int getY(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    // 左上角y坐标 - Upper left corner y coordinate
    int y = (int) (rect.getY() * img.getHeight());
    return y;
  }
}
