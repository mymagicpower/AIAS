package me.aias.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class DJLImageUtils {

  public static Image bufferedImage2DJLImage(BufferedImage img) {
    return ImageFactory.getInstance().fromImage(img);
  }

  public static void saveImage(BufferedImage img, String name, String path) {
    Image djlImg = ImageFactory.getInstance().fromImage(img);
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    try {
      djlImg.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveDJLImage(Image img, String name, String path) {
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    try {
      img.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveBoundingBoxImage(
      Image img, DetectedObjects detection, String name, String path) throws IOException {
    // Make imageName copy with alpha channel because original imageName was jpg
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    img.save(Files.newOutputStream(imagePath), "png");
  }

  public static void drawImageRect(BufferedImage image, int x, int y, int width, int height) {
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(new Color(246, 96, 0));
      BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
      g.setStroke(bStroke);
      g.drawRect(x, y, width, height);

    } finally {
      g.dispose();
    }
  }

  public static void drawImageRect(
      BufferedImage image, int x, int y, int width, int height, Color c) {
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(c);
      BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
      g.setStroke(bStroke);
      g.drawRect(x, y, width, height);

    } finally {
      g.dispose();
    }
  }

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
}
