package me.aias.util;

import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;

import java.awt.image.BufferedImage;
import java.util.List;

public class FaceUtils {

  /** 返回外扩100%人脸 factor = 1, 100%, factor = 0.2, 20% */
  public static Rectangle getSubImageRect(
      Rectangle rectangle, int width, int height, float factor) {
    // 左上角坐标
    int x1 = (int) (rectangle.getX() * width);
    int y1 = (int) (rectangle.getY() * height);
    // 宽度，高度
    int w = (int) (rectangle.getWidth() * width);
    int h = (int) (rectangle.getHeight() * height);
    // 左上角坐标
    int x2 = x1 + w;
    int y2 = y1 + h;

    // 外扩，防止对齐后人脸出现黑边
    int new_x1 = Math.max((int) (x1 + x1 * factor / 2 - x2 * factor / 2), 0);
    int new_x2 = Math.min((int) (x2 + x2 * factor / 2 - x1 * factor / 2), width - 1);
    int new_y1 = Math.max((int) (y1 + y1 * factor / 2 - y2 * factor / 2), 0);
    int new_y2 = Math.min((int) (y2 + y2 * factor / 2 - y1 * factor / 2), height - 1);
    int new_w = new_x2 - new_x1;
    int new_h = new_y2 - new_y1;

    // double x, double y, double width, double height
    Rectangle newRect = new Rectangle(new_x1, new_y1, new_w, new_h);

    return newRect;
  }

  public static double[][] pointsArray(Rectangle subImageRect, Iterable<Point> points) {
    int x = (int) (subImageRect.getX());
    int y = (int) (subImageRect.getY());
    //      图中关键点坐标
    //      1.  left_eye_x , left_eye_y
    //      2.  right_eye_x , right_eye_y
    //      3.  nose_x , nose_y
    //      4.  left_mouth_x , left_mouth_y
    //      5.  right_mouth_x , right_mouth_y
    double[][] pointsArray = new double[5][2]; // 保存人脸关键点
    int i = 0;
    for (Point point : points) {
      pointsArray[i][0] = point.getX() - x;
      pointsArray[i][1] = point.getY() - y;
      i++;
    }
    return pointsArray;
  }
}
