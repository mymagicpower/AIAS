package me.aias.example.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUtils {

  /**
   * BufferedImage图片格式转DJL图片格式
   * Convert BufferedImage image format to DJL image format
   *
   * @author Calvin
   */
  public static Image convert(BufferedImage img) {
    return ImageFactory.getInstance().fromImage(img);
  }

  /**
   * 保存BufferedImage图片
   * Save BufferedImage image
   *
   * @author Calvin
   */
  public static void saveImage(BufferedImage img, String name, String path) {
    Image djlImg = ImageFactory.getInstance().fromImage(img); // 支持多种图片格式，自动适配
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    try {
      djlImg.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 保存DJL图片
   * Save DJL image
   *
   * @author Calvin
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
   * 保存图片,含检测框
   * Save image with bounding box
   *
   * @author Calvin
   */
  public static void saveBoundingBoxImage(
          Image img, DetectedObjects detection, String name, String path) throws IOException {
    // Make image copy with alpha channel because original image was jpg
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK can't save jpg with alpha channel
    img.save(Files.newOutputStream(imagePath), "png");
  }

  /**
   * 绘制人脸关键点
   * Draw facial landmarks
   *
   * @author Calvin
   */
  public static void drawLandmark(Image img, BoundingBox box, float[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      int x = getX(img, box, array[2 * i]);
      int y = getY(img, box, array[2 * i + 1]);
      Color c = new Color(0, 255, 0);
      drawImageRect((BufferedImage) img.getWrappedImage(), x, y, 1, 1, c);
    }
  }

  /**
   * 画检测框
   * Draw bounding box
   *
   * @author Calvin
   */
  public static void drawImageRect(BufferedImage image, int x, int y, int width, int height) {
    // 将绘制图像转换为Graphics2D
    // Convert the drawing image to Graphics2D
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(new Color(246, 96, 0));
      // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
      // Declare brush properties: thickness (in pixels) without end decoration, at the fold with sharp corners
      BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
      g.setStroke(bStroke);
      g.drawRect(x, y, width, height);

    } finally {
      g.dispose();
    }
  }

  /**
   * 画检测框
   * Draw bounding box
   *
   * @author Calvin
   */
  public static void drawImageRect(
          BufferedImage image, int x, int y, int width, int height, Color c) {
    // 将绘制图像转换为Graphics2D
    // Convert the drawing image to Graphics2D
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(c);
      // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
      // Declare brush properties: thickness (in pixels) without end decoration, at the fold with sharp corners
      BasicStroke bStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
      g.setStroke(bStroke);
      g.drawRect(x, y, width, height);

    } finally {
      g.dispose();
    }
  }

  /**
   * 显示文字
   * Show text
   *
   * @author Calvin
   */
  public static void drawImageText(BufferedImage image, String text) {
    Graphics graphics = image.getGraphics();
    int fontSize = 100;
    Font font = new Font("楷体", Font.PLAIN, fontSize);
    try {
      graphics.setFont(font);
      graphics.setColor(new Color(246, 96, 0));
      int strWidth = graphics.getFontMetrics().stringWidth(text);
      graphics.drawString(text, fontSize - (strWidth / 2), fontSize + 30);
    } finally {
      graphics.dispose();
    }
  }

  /**
   * 返回外扩人脸 factor = 1, 100%, factor = 0.2, 20%
   * Returns the outward expansion of the face factor = 1, 100%, factor = 0.2, 20%
   *
   * @author Calvin
   */
  public static Image getSubImage(Image img, BoundingBox box, float factor) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    // Upper left corner coordinate
    int x1 = (int) (rect.getX() * img.getWidth());
    int y1 = (int) (rect.getY() * img.getHeight());
    // 宽度，高度
    // Width and height
    int w = (int) (rect.getWidth() * img.getWidth());
    int h = (int) (rect.getHeight() * img.getHeight());
    // 右下角坐标
    // Lower right corner coordinate
    int x2 = x1 + w;
    int y2 = y1 + h;

    // 外扩大100%，防止对齐后人脸出现黑边
    // Expand 100% to prevent black edges on the face after alignment
    int new_x1 = Math.max((int) (x1 + x1 * factor / 2 - x2 * factor / 2), 0);
    int new_x2 = Math.min((int) (x2 + x2 * factor / 2 - x1 * factor / 2), img.getWidth() - 1);
    int new_y1 = Math.max((int) (y1 + y1 * factor / 2 - y2 * factor / 2), 0);
    int new_y2 = Math.min((int) (y2 + y2 * factor / 2 - y1 * factor / 2), img.getHeight() - 1);
    int new_w = new_x2 - new_x1;
    int new_h = new_y2 - new_y1;

    return img.getSubImage(new_x1, new_y1, new_w, new_h);
  }

  private static int getX(Image img, BoundingBox box, float x) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    // Upper left corner coordinate
    int x1 = (int) (rect.getX() * img.getWidth());
    // 宽度
    // Width
    int w = (int) (rect.getWidth() * img.getWidth());

    return (int) (x * w + x1);
  }

  private static int getY(Image img, BoundingBox box, float y) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    // Upper left corner coordinate
    int y1 = (int) (rect.getY() * img.getHeight());
    // 高度
    // Height
    int h = (int) (rect.getHeight() * img.getHeight());

    return (int) (y * h + y1);
  }
}