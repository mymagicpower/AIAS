package me.calvin.example;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import me.calvin.aias.FaceDetection;
import me.calvin.aias.FaceLandmark;
import me.calvin.aias.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class FaceLandmarkExample {

  private static final Logger logger = LoggerFactory.getLogger(FaceLandmarkExample.class);

  private FaceLandmarkExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/faces.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    DetectedObjects detections = new FaceDetection().predict(image);
    List<DetectedObjects.DetectedObject> faces = detections.items();
    //    List<String> names = new ArrayList<>();
    //    List<Double> prob = new ArrayList<>();
    //    List<BoundingBox> rect = new ArrayList<>();
    int index = 0;

    for (DetectedObjects.DetectedObject face : faces) {
      // 外扩人脸比例 factor = 1, 100%, factor = 0.2, 20%
      Image subImg = ImageUtils.getSubImage(image, face.getBoundingBox(), 0f);
      saveImage(subImg, "face_" + index++ + ".png", "build/output");
      float[][] result = new FaceLandmark().predict(subImg);

      logger.info("{}", Arrays.toString(result[0]));
      drawLandmark(image, face.getBoundingBox(), result[0]);

      //      names.add(face.getClassName());
      //      prob.add(face.getProbability());
      //      rect.add(face.getBoundingBox());
    }
    drawBoundingBoxImage(image, detections);
    saveImage(image, "face-landmarks.png", "build/output");

    logger.info("{}", detections);
  }

  private static void drawLandmark(Image img, BoundingBox box, float[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      int x = getX(img, box, array[2 * i]);
      int y = getY(img, box, array[2 * i + 1]);
      Color c = new Color(0, 255, 0);
      ImageUtils.drawImageRect((BufferedImage) img.getWrappedImage(), x, y, 2, 2, c);
    }
  }

  private static void drawBoundingBoxImage(Image img, DetectedObjects detection) {
    img.drawBoundingBoxes(detection);
  }

  public static void saveImage(Image img, String name, String path) {
    Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK 不能保存 jpg 图片的 alpha channel
    try {
      newImage.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int getX(Image img, BoundingBox box, float x) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    int x1 = (int) (rect.getX() * img.getWidth());
    // 宽度
    int w = (int) (rect.getWidth() * img.getWidth());

    return (int) (x * w + x1);
  }

  public static int getY(Image img, BoundingBox box, float y) {
    Rectangle rect = box.getBounds();
    // 左上角坐标
    int y1 = (int) (rect.getY() * img.getHeight());
    // 高度
    int h = (int) (rect.getHeight() * img.getHeight());

    return (int) (y * h + y1);
  }
}
