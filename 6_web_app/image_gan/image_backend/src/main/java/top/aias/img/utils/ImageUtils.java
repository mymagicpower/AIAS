package top.aias.img.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 图像工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class ImageUtils {
    /**
     * 保存BufferedImage图片
     *
     * @param img
     * @param name
     * @param path
     */
    public static void saveBufferedImage(BufferedImage img, String name, String path) {
        Path outputDir = Paths.get(path);
        Path imagePath = outputDir.resolve(name);
        try {
            ImageIO.write(img, "png", new File(imagePath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存DJL图片
     *
     * @param img
     * @param name
     * @param path
     */
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

    /**
     * 保存图片,含检测框
     *
     * @param img
     * @param detection
     * @param name
     * @param path
     * @throws IOException
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
     * 画矩形
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     */
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

    /**
     * 画矩形
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param c
     */
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

    /**
     * 显示文字
     * @param image
     * @param text
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
     * @param img
     * @param box
     * @param factor
     * @return
     */
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

    public static BufferedImage removeBg(BufferedImage image) {
        // 获取图片的宽度和高度
        int width = image.getWidth();
        int height = image.getHeight();

        // 创建一个新的BufferedImage，类型为ARGB，用于存储转换后的图片
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 遍历每个像素
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 获取当前像素的RGB值
                int rgba = image.getRGB(x, y);
                // 将RGB值转换为颜色对象
                Color color = new Color(rgba, true);
                // 如果当前像素是黑色，则将Alpha通道值设置为0
                if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0) {
                    color = new Color(0, 0, 0, 0);
                }

                // 将修改后的颜色设置到新的BufferedImage中
                newImage.setRGB(x, y, color.getRGB());
            }
        }
        return newImage;
    }

    /**
     * BufferedImage 转 base64
     * @param image
     * @param type - JPG、PNG、GIF、BMP 等
     * @return
     * @throws IOException
     */
    public static String toBase64(BufferedImage image, String type) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, type, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
