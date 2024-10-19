package top.aias.img.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * 背景转透明
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TransparentBG {
    public static void main(String[] args) {
        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File("build/output/isnet-anime.png"));

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

            // 保存修改后的图片
            ImageIO.write(newImage, "PNG", new File("build/output/output2.png"));

            System.out.println("转换完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
