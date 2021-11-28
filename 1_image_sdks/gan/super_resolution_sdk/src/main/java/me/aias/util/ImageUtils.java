package me.aias.util;

import ai.djl.engine.Engine;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Joints;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.util.RandomUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ImageUtils {

  public static Image bufferedImage2DJLImage(BufferedImage img) {
    return ImageFactory.getInstance().fromImage(img);
  }

  public static void saveImages(List<Image> input, List<Image> generated, String path) throws IOException {
    Path outputPath = Paths.get(path);
    Files.createDirectories(outputPath);

    save(generated, "image", outputPath);
    save(group(input, generated), "stitch", outputPath);

  }

  private static void save(List<Image> images, String name, Path path) throws IOException {
    for (int i = 0; i < images.size(); i++) {
      Path imagePath = path.resolve(name + i + ".png");
      images.get(i).save(Files.newOutputStream(imagePath), "png");
    }
  }

  private static List<Image> group(List<Image> input, List<Image> generated) {
    NDList stitches = new NDList(input.size());

    try (NDManager manager = Engine.getInstance().newBaseManager()) {
      for (int i = 0; i < input.size(); i++) {
        int scale = 4;
        int width = scale * input.get(i).getWidth();
        int height = scale * input.get(i).getHeight();

        NDArray left = input.get(i).toNDArray(manager);
        NDArray right = generated.get(i).toNDArray(manager);

        left = NDImageUtils.resize(left, width, height, Image.Interpolation.BICUBIC);
        left = left.toType(DataType.UINT8, false);

        right = right.toType(DataType.UINT8, false);

        stitches.add(NDArrays.concat(new NDList(left, right), 1));
      }

      return stitches.stream()
              .map(array -> array.toType(DataType.UINT8, false))
              .map(array -> ImageFactory.getInstance().fromNDArray(array))
              .collect(Collectors.toList());
    }
  }

  public static void saveImage(BufferedImage img, String name, String path) {
    ai.djl.modality.cv.Image djlImg = ImageFactory.getInstance().fromImage(img); // 支持多种图片格式，自动适配
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK 不能保存 jpg 图片的 alpha channel
    try {
      djlImg.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveImage(Image img, String name, String path) {
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK 不能保存 jpg 图片的 alpha channel
    try {
      img.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveBoundingBoxImage(
          Image img, DetectedObjects detection, String name, String path) throws IOException {
    img.drawBoundingBoxes(detection);
    Path outputDir = Paths.get(path);
    Files.createDirectories(outputDir);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK can't save jpg with alpha channel
    img.save(Files.newOutputStream(imagePath), "png");
  }

  public static void drawImageRect(BufferedImage image, int x, int y, int width, int height) {
    // 将绘制图像转换为Graphics2D
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(new Color(246, 96, 0));
      // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
      BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
      g.setStroke(bStroke);
      g.drawRect(x, y, width, height);

    } finally {
      g.dispose();
    }
  }

  public static void drawImageRect(
      BufferedImage image, int x, int y, int width, int height, Color c) {
    // 将绘制图像转换为Graphics2D
    Graphics2D g = (Graphics2D) image.getGraphics();
    try {
      g.setColor(c);
      // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
      BasicStroke bStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
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

  /** 返回外扩人脸 factor = 1, 100%, factor = 0.2, 20% */
  public static Image getSubImage(Image img, BoundingBox box, float factor) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    int x1 = (int) (rect.getX() * img.getWidth());
    int y1 = (int) (rect.getY() * img.getHeight());
    // 宽度，高度
    int w = (int) (rect.getWidth() * img.getWidth());
    int h = (int) (rect.getHeight() * img.getHeight());
    // 左上角坐标
    int x2 = x1 + w;
    int y2 = y1 + h;

    // 外扩大100%，防止对齐后人脸出现黑边
    int new_x1 = Math.max((int) (x1 + x1 * factor / 2 - x2 * factor / 2), 0);
    int new_x2 = Math.min((int) (x2 + x2 * factor / 2 - x1 * factor / 2), img.getWidth() - 1);
    int new_y1 = Math.max((int) (y1 + y1 * factor / 2 - y2 * factor / 2), 0);
    int new_y2 = Math.min((int) (y2 + y2 * factor / 2 - y1 * factor / 2), img.getHeight() - 1);
    int new_w = new_x2 - new_x1;
    int new_h = new_y2 - new_y1;

    return img.getSubImage(new_x1, new_y1, new_w, new_h);
  }

  public static void drawJoints(Image img, Image subImg, int x, int y, Joints joints) {
    BufferedImage image = (BufferedImage) img.getWrappedImage();
    Graphics2D g = (Graphics2D) image.getGraphics();
    int stroke = 2;
    g.setStroke(new BasicStroke((float) stroke));
    int imageWidth = subImg.getWidth();
    int imageHeight = subImg.getHeight();
    Iterator iterator = joints.getJoints().iterator();

    while (iterator.hasNext()) {
      Joints.Joint joint = (Joints.Joint) iterator.next();
      g.setPaint(randomColor().darker());
      int newX = x + (int) (joint.getX() * (double) imageWidth);
      int newY = y + (int) (joint.getY() * (double) imageHeight);
      g.fillOval(newX, newY, 10, 10);
    }

    g.dispose();
  }

  private static Color randomColor() {
    return new Color(RandomUtils.nextInt(255));
  }

  public static void drawBoundingBoxImage(Image img, DetectedObjects detection) {
    img.drawBoundingBoxes(detection);
  }

  public static int getX(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    // 左上角x坐标
    int x = (int) (rect.getX() * img.getWidth());
    return x;
  }

  public static int getY(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    // 左上角y坐标
    int y = (int) (rect.getY() * img.getHeight());
    return y;
  }
}
